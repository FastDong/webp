package ce.mnu.wptc.dto; // 또는 view

import lombok.Data;

@Data
public class VirtualStockView {
    private Long stockId;
    private String stockName;
    private long price;
    private long userQuantity;
}
