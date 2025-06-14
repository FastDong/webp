package ce.mnu.wptc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Stock;
import ce.mnu.wptc.entity.UserAsset;

@Repository
public interface UserAssetRepository extends JpaRepository<UserAsset, Long> {

    // 특정 회원의 모든 자산 조회
    List<UserAsset> findByMember(Member member);

    // 특정 회원의 특정 주식 자산 조회
    Optional<UserAsset> findByMemberAndStock(Member member, Stock stock);

    // 특정 회원이 보유한 주식 종목 수
    @Query("SELECT COUNT(ua) FROM UserAsset ua WHERE ua.member = :member AND ua.quantity > 0")
    long countByMemberAndQuantityGreaterThan(Member member);
}
