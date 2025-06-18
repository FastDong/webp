    package ce.mnu.wptc.service;

    import ce.mnu.wptc.dto.MainPageData;
    import ce.mnu.wptc.dto.VirtualStockView;
    import ce.mnu.wptc.entity.Member;
    import ce.mnu.wptc.entity.Post;
    import ce.mnu.wptc.entity.Stocks;
    import ce.mnu.wptc.repository.MemberRepository;
    import ce.mnu.wptc.repository.PostRepository;
    import ce.mnu.wptc.repository.StocksRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;
    import ce.mnu.wptc.dto.OwnedStockView;

    @Service
    @RequiredArgsConstructor
    public class MainPageService {

        private final PostRepository postRepository;
        private final MemberRepository memberRepository;
        private final StocksRepository stocksRepository;
        private final StockDataService stockDataService;

        // 데이터를 조회만 하므로 readOnly=true로 성능을 최적화합니다.
        @Transactional(readOnly = true)
        public MainPageData prepareMainPageData(Long memberId, Pageable pageable) {
            // 1. 게시판 데이터 로드
            Page<Post> postPage = postRepository.findAll(pageable);

            // 2. 로그인 회원 정보 로드
            Member member = (memberId != null) ? memberRepository.findById(memberId).orElse(null) : null;

            // 3. 주식 목록(VirtualStockView) 생성
            List<Stocks> virtualStocks = stocksRepository.findByMemberIsNull();
            List<VirtualStockView> stockList = virtualStocks.stream()
                    .map(stock -> createVirtualStockView(stock, member))
                    .collect(Collectors.toList());

            // ✅ 회원의 보유 주식 목록을 OwnedStockView DTO 리스트로 변환
            List<OwnedStockView> ownedStockViewList = new ArrayList<>();
            long totalStockValue = 0;
            long totalPurchasePrice = 0; // ✅ 총 매수 금액을 저장할 변수 초기화

            if (member != null) {
                Map<String, Long> virtualStockPrices = virtualStocks.stream()
                        .collect(Collectors.toMap(Stocks::getStockName, Stocks::getPrice));

                List<Stocks> ownedStocks = stocksRepository.findByMember(member);

                for (Stocks stock : ownedStocks) {
                    long currentPrice = virtualStockPrices.getOrDefault(stock.getStockName(), stock.getPrice());
                    long totalPurchase = stock.getPrice() * stock.getCount();
                    long totalCurrent = currentPrice * stock.getCount();
                    long profitLoss = totalCurrent - totalPurchase;
                    double profitRate = (totalPurchase == 0) ? 0 : ((double) profitLoss / totalPurchase) * 100.0;

                    // ✅ 평단가 계산 로직 추가
                    // 보유 수량이 0보다 클 때만 '총 매수금액 / 수량'으로 계산
                    long averagePrice = (stock.getCount() > 0) ? (totalPurchase / stock.getCount()) : 0;


                    ownedStockViewList.add(OwnedStockView.builder()
                            .stockName(stock.getStockName())
                            .quantity(stock.getCount())
                            .totalPurchasePrice(totalPurchase)
                            .totalCurrentValue(totalCurrent)
                            .profitLossAmount(profitLoss)
                            .profitLossRate(profitRate)
                            .averagePurchasePrice(averagePrice) // ✅ 계산된 평단가를 DTO에 추가
                            .build());

                    totalStockValue += totalCurrent;
                    totalPurchasePrice += totalPurchase; // ✅ 전체 포트폴리오의 총 매수 금액 누적
                }
            }
            // ✅ 포트폴리오 전체의 손익 및 수익률 계산
            long totalProfitLoss = totalStockValue - totalPurchasePrice;
            double totalProfitLossRate = (totalPurchasePrice == 0) ? 0 : ((double) totalProfitLoss / totalPurchasePrice) * 100.0;

            // 5. 모든 데이터를 MainPageData DTO에 담아 반환
            return MainPageData.builder()
                    .postPage(postPage)
                    .member(member)
                    .stockList(stockList)
                    .ownedStockViewList(ownedStockViewList) // ✅ 수정된 DTO 리스트 전달
                    .totalStockValue(totalStockValue)
                    .totalPurchasePrice(totalPurchasePrice) // 새로 계산한 값 전달
                    .totalProfitLoss(totalProfitLoss)       // 새로 계산한 값 전달
                    .totalProfitLossRate(totalProfitLossRate) // 새로 계산한 값 전달
                    .build();
        }

        /**
         * ✅ 실시간 주식 뷰 DTO 리스트를 반환하는 새로운 메서드 추가
         * @param memberId 현재 로그인한 회원의 ID (비로그인 시 null)
         * @return VirtualStockView DTO 리스트
         */
        @Transactional(readOnly = true)
        public List<VirtualStockView> getRealTimeStockViews(Long memberId) {
            // 1. 로그인 회원 정보 로드
            Member member = (memberId != null) ? memberRepository.findById(memberId).orElse(null) : null;

            // 2. 가상 주식 목록을 DTO로 변환하여 반환 (기존 private 메서드 재활용)
            return stocksRepository.findByMemberIsNull().stream()
                    .map(stock -> createVirtualStockView(stock, member))
                    .collect(Collectors.toList());
        }


        // VirtualStockView DTO를 생성하는 보조 메서드
        private VirtualStockView createVirtualStockView(Stocks stock, Member member) {
            VirtualStockView dto = new VirtualStockView();
            dto.setStockId(stock.getStockId());
            dto.setStockName(stock.getStockName());
            dto.setPrice(stock.getPrice());

            long userQuantity = 0;
            if (member != null) {
                userQuantity = stocksRepository.findByMemberAndStockName(member, stock.getStockName())
                        .map(Stocks::getCount).orElse(0L);
            }
            dto.setUserQuantity(userQuantity);

            StockDataService.PriceChangeData changeData = stockDataService.getPriceChange(stock.getStockName());
            dto.setPriceChangeAmount(changeData.amount());
            dto.setPriceChangeRate(changeData.rate());

            return dto;
        }

        // 총 주식 평가액을 계산하는 보조 메서드
        private long calculateTotalStockValue(List<Stocks> ownedStocks, List<Stocks> virtualStocks) {
            Map<String, Long> prices = virtualStocks.stream()
                    .collect(Collectors.toMap(Stocks::getStockName, Stocks::getPrice));

            return ownedStocks.stream()
                    .mapToLong(stock -> stock.getCount() * prices.getOrDefault(stock.getStockName(), stock.getPrice()))
                    .sum();
        }
    }
