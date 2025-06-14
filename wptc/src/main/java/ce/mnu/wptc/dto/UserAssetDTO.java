package ce.mnu.wptc.dto;

import lombok.*;
import java.math.BigDecimal;

import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "USER_ASSET",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "stock_id"})
})
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
