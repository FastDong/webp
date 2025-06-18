package ce.mnu.wptc.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StockDataService {

    // 스레드 환경에 안전한 ConcurrentHashMap을 사용합니다.
    // Key: 종목명 (String), Value: 변동 데이터 (PriceChangeData)
    private final Map<String, PriceChangeData> priceChanges = new ConcurrentHashMap<>();

    // 변동 데이터를 업데이트하는 메서드
    public void updatePriceChange(String stockName, long amount, double rate) {
        priceChanges.put(stockName, new PriceChangeData(amount, rate));
    }

    // 변동 데이터를 가져오는 메서드
    public PriceChangeData getPriceChange(String stockName) {
        // 데이터가 없는 경우를 대비해 기본값(변동 없음)을 반환합니다.
        return priceChanges.getOrDefault(stockName, new PriceChangeData(0, 0.0));
    }

    // 변동 데이터를 담을 간단한 내부 클래스(Record)
    public record PriceChangeData(long amount, double rate) {}
}
