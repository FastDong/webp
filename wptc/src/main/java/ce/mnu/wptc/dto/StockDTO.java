package ce.mnu.wptc.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDTO {
    private Long stockId;
    private String stockCode;
    private String stockName;
    private BigDecimal currentPrice;
}
