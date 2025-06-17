package ce.mnu.wptc.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "STOCKS")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stocks {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long stockId;
	private String stockName;
	private long count = 0;
	private long price = 0;
	
	public Stocks(String stockName, long count, long price) {
		this.stockName = stockName;
		this.count = count;
		this.price= price;
	}
	
}
