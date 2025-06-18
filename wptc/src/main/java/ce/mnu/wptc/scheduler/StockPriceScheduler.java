package ce.mnu.wptc.scheduler;

import ce.mnu.wptc.entity.Stocks;
import ce.mnu.wptc.repository.StocksRepository;
import ce.mnu.wptc.service.StockDataService; // ✅ 서비스 임포트
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockPriceScheduler {

    private final StocksRepository stocksRepository;
    private final StockDataService stockDataService; // ✅ 서비스 주입

    @Scheduled(fixedRate = 3000) // 10분마다 실행
    public void updateStockPrices() {
        List<Stocks> virtualStocks = stocksRepository.findByMemberIsNull();

        for (Stocks stock : virtualStocks) {
            long oldPrice = stock.getPrice();

            // 랜덤 변동률로 새 가격 계산 (-3% ~ +3%)
            double changeRatePercent = (Math.random() * 6.0) - 3.0;
            long priceChangeAmount = (long) (oldPrice * (changeRatePercent / 100.0));
            long newPrice = oldPrice + priceChangeAmount;

            stock.setPrice(newPrice);
            stocksRepository.save(stock);

            // ✅ 계산된 변동 데이터를 서비스에 즉시 업데이트
            stockDataService.updatePriceChange(stock.getStockName(), priceChangeAmount, changeRatePercent);
        }
    }
}
