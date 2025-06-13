package ce.mnu.wptc.repository;

import ce.mnu.wptc.entity.Grade;
import ce.mnu.wptc.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 로그인 ID로 회원 조회
    Optional<Member> findByLoginId(String loginId);

    // 이메일로 회원 조회
    Optional<Member> findByEmail(String email);

    // 닉네임으로 회원 조회
    Optional<Member> findByNickname(String nickname);

    // 로그인 ID 중복 체크
    boolean existsByLoginId(String loginId);

    // 이메일 중복 체크
    boolean existsByEmail(String email);

    // 닉네임 중복 체크
    boolean existsByNickname(String nickname);

    // 특정 등급의 회원들 조회
    List<Member> findByGrade(Grade grade);

    // 포인트 범위로 회원 조회
    List<Member> findByPointBetween(int minPoint, int maxPoint);

    // 이메일 인증된 회원들 조회
    List<Member> findByEmailVerifiedTrue();

    // 포인트 순으로 상위 회원 조회 (랭킹)
    @Query("SELECT m FROM Member m ORDER BY m.point DESC")
    List<Member> findTopMembersByPoint();
}
