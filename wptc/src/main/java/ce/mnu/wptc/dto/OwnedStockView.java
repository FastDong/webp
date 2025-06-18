package ce.mnu.wptc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OwnedStockView {
    private String stockName;
    private long quantity;          // 보유 수량
    private long totalPurchasePrice; // 총 매수 금액 (평균단가 * 수량)
    private long totalCurrentValue;  // 총 평가 금액 (현재가 * 수량)
    private long profitLossAmount;   // 평가 손익
    private double profitLossRate;   // 수익률
    private long averagePurchasePrice; // 매수 평균 단가 (평단가)
}
