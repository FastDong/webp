package ce.mnu.wptc.repository;

import ce.mnu.wptc.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    // 등급명으로 조회
    Optional<Grade> findByGradeName(String gradeName);

    // 필요 포인트로 적절한 등급 찾기 (포인트 이하의 가장 높은 등급)
    @Query("SELECT g FROM Grade g WHERE g.requiredPoint <= :point ORDER BY g.requiredPoint DESC")
    List<Grade> findGradesByPoint(int point);

    // 필요 포인트 순으로 모든 등급 조회
    List<Grade> findAllByOrderByRequiredPointAsc();
}
