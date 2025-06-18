package ce.mnu.wptc.dto;

import lombok.Data;

@Data
public class VirtualStockView {
    private Long stockId;
    private String stockName;
    private long price;
    private long userQuantity;

    // ✅ 변동 데이터를 담을 필드 추가
    private long priceChangeAmount;
    private double priceChangeRate;
}
