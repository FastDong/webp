package ce.mnu.wptc.repository;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GRADE") // 실제 DB 테이블명 지정
@Data
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB의 auto_increment 기능 사용
    @Column(name = "grade_id")
    private Long id;

    @Column(name = "grade_name", nullable = false, length = 50)
    private String gradeName;

    @Column(name = "required_point", nullable = false)
    private int requiredPoint;
    
    // Grade(1)가 Member(N)를 가짐
    @OneToMany(mappedBy = "grade")
    private List<Member> members = new ArrayList<>();

}