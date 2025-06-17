package ce.mnu.wptc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "STOCKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stocks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stockId;

    private String stockName;

    private long count = 0;

    private long price = 0;

    // 🔻 회원과의 다대일 관계 (MEMBER_ID 외래키 매핑)
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")  // STOCKS 테이블의 외래키 컬럼명
    private Member member;

    // 생성자에서 member도 설정할 수 있는 버전 추가
    public Stocks(String stockName, long count, long price, Member member) {
        this.stockName = stockName;
        this.count = count;
        this.price = price;
        this.member = member;
    }
}
