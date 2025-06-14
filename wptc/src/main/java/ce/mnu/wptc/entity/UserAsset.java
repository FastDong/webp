package ce.mnu.wptc.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "USER_ASSET")
@Getter
@Setter // 자산 정보는 거래 시 변경되므로 Setter 허용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "stock"})
@AllArgsConstructor
@Builder
public class UserAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_asset_id")
    private Long userAssetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "average_purchase_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal averagePurchasePrice;
}
