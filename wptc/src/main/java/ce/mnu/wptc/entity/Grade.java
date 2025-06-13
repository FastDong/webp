package ce.mnu.wptc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GRADE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long gradeId;

    @Column(name = "grade_name", length = 50, nullable = false)
    private String gradeName;

    @Column(name = "required_point", nullable = false)
    private int requiredPoint;
}
