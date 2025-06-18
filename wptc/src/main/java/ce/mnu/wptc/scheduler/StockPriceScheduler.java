package ce.mnu.wptc.scheduler;

import ce.mnu.wptc.entity.Stocks;
import ce.mnu.wptc.repository.StocksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StockPriceScheduler {

    private final StocksRepository stocksRepository;

    // 10분마다 실행 (600,000ms)
    @Scheduled(fixedRate = 600_000)
    public void updateStockPrices() {
        // 공용 가상주식(Member=null) 조회
        List<Stocks> virtualStocks = stocksRepository.findByMemberIsNull();

        for (Stocks stock : virtualStocks) {
            // 랜덤 변동률 (-3% ~ +3%)
            double changeRate = (Math.random() * 0.06) - 0.03;
            long newPrice = (long) (stock.getPrice() * (1 + changeRate));
            stock.setPrice(newPrice);
            stocksRepository.save(stock);
        }
    }
}
