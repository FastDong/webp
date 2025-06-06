package ce.mnu.wptc.repository;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "MEMBER")
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id", unique = true, nullable = false, length = 50)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;
    
    // Member(N)가 Grade(1)를 참조
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: grade가 실제로 필요할 때만 DB에서 조회
    @JoinColumn(name = "grade_id") // MEMBER 테이블의 외래 키 컬럼명
    private Grade grade;
    
}