package ce.mnu.wptc.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAssetDTO {
    private Long userAssetId;
    private Long memberId;
    private Long stockId;
    private String stockCode;
    private String stockName;
    private int quantity;
    private BigDecimal averagePurchasePrice;
    private BigDecimal currentPrice;
    private BigDecimal totalValue; // 현재 총 가치
    private BigDecimal profitLoss; // 손익
}
