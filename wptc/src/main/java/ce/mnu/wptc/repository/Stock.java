package ce.mnu.wptc.repository;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "STOCK")
public class Stock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_id")
	private Long stockId;
	
	@Column(length = 20, name = "stock_code", unique = true, nullable = false)
	private String stockCode;
	
	@Column(length = 100, name = "stock_name", nullable = false)
	private String stockName;
	
	@Column(name = "curent_price", nullable = false, precision =15, scale = 2)
	private BigDecimal currentPrice;
}
