package ce.mnu.wptc.service;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Stocks;
import ce.mnu.wptc.repository.MemberRepository;
import ce.mnu.wptc.repository.StocksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final MemberRepository memberRepository;
    private final StocksRepository stocksRepository;

    @Transactional
    public void buyStock(Long memberId, Long stockId, long quantity) {
        // 1. 회원과 가상 주식 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Stocks virtualStock = stocksRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주식입니다."));

        long price = virtualStock.getPrice();
        long totalPrice = price * quantity;

        // 2. 구매 가능 여부 확인 (포인트 부족)
        if (member.getPoint() < totalPrice) {
            throw new IllegalStateException("포인트가 부족합니다.");
        }

        // 3. 회원 포인트 차감
        member.setPoint(member.getPoint() - totalPrice);
        memberRepository.save(member);

        // 4. 회원의 보유 주식 정보 업데이트
        Optional<Stocks> userStockOpt = stocksRepository.findByMemberAndStockName(member, virtualStock.getStockName());

        if (userStockOpt.isPresent()) {
            // 이미 보유한 주식이면 수량만 추가
            Stocks userStock = userStockOpt.get();
            userStock.setCount(userStock.getCount() + quantity);
            stocksRepository.save(userStock);
        } else {
            // 처음 매수하는 주식이면 새로 생성
            Stocks newUserStock = new Stocks(virtualStock.getStockName(), quantity, price, member);
            stocksRepository.save(newUserStock);
        }
    }

    @Transactional
    public void sellStock(Long memberId, Long stockId, long quantity) {
        // 1. 회원과 판매할 주식 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Stocks virtualStock = stocksRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주식입니다."));

        Stocks userStock = stocksRepository.findByMemberAndStockName(member, virtualStock.getStockName())
                .orElseThrow(() -> new IllegalStateException("보유하지 않은 주식입니다."));

        // 2. 판매 가능 여부 확인 (보유 수량 부족)
        if (userStock.getCount() < quantity) {
            throw new IllegalStateException("보유 수량이 부족합니다.");
        }

        // 3. 회원 포인트 증가
        long totalPrice = virtualStock.getPrice() * quantity;
        member.setPoint(member.getPoint() + totalPrice);
        memberRepository.save(member);

        // 4. 보유 주식 수량 감소
        userStock.setCount(userStock.getCount() - quantity);

        if (userStock.getCount() == 0) {
            // 보유 수량이 0이 되면 삭제
            stocksRepository.delete(userStock);
        } else {
            stocksRepository.save(userStock);
        }
    }
}
