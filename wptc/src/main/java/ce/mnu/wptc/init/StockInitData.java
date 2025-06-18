package ce.mnu.wptc.init;

import ce.mnu.wptc.entity.Stocks;
import ce.mnu.wptc.repository.StocksRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockInitData {

    private final StocksRepository stocksRepository;

    @PostConstruct
    public void init() {
        if (stocksRepository.findByMemberIsNull().size() == 0) {
            stocksRepository.save(new Stocks("삼성전자", 0, 75000, null));
            stocksRepository.save(new Stocks("SK하이닉스", 0, 120000, null));
            stocksRepository.save(new Stocks("NAVER", 0, 180000, null));
            stocksRepository.save(new Stocks("카카오", 0, 45000, null));
            stocksRepository.save(new Stocks("LG화학", 0, 350000, null));
        }
    }
}
