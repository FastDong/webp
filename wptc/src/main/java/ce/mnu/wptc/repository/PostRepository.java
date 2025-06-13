package ce.mnu.wptc.repository;

import ce.mnu.wptc.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 회원의 게시글 조회
    List<Post> findByMemberOrderByCreatedAtDesc(Member member);

    // 조회수 순으로 인기 게시글 조회
    List<Post> findTop10ByOrderByViewCountDesc();

    // 내용으로 게시글 검색
    List<Post> findByContentContainingOrderByCreatedAtDesc(String keyword);

    // 회원별 게시글 수 조회
    @Query("SELECT COUNT(p) FROM Post p WHERE p.member = :member")
    long countByMember(Member member);
}
