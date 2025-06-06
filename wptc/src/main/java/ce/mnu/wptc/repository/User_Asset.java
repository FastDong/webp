package ce.mnu.wptc.repository;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_ASSET",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"member_id", "stock_id"})
		})
@Data
public class User_Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_asset_id")
    private Long id;

 // USER_ASSET(N) : STOCK(1) 관계
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "stock_id", nullable = false) 
    private Stock stock;

 //
    // USER_ASSET(N) : MEMBER(1) 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Column(name = "quantity", nullable = false)
    private int quantity;
  
    @Column(name = "average_purchase_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal average_purchase_price;
}