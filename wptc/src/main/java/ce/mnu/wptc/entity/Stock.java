package ce.mnu.wptc.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "STOCK")
@Getter
@Setter // 주식 가격은 실시간으로 변경되므로 Setter 허용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long stockId;

    @Column(name = "stock_code", length = 20, nullable = false, unique = true)
    private String stockCode;

    @Column(name = "stock_name", length = 100, nullable = false)
    private String stockName;

    @Column(name = "current_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal currentPrice;
}
