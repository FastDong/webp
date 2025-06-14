package ce.mnu.wptc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // PostService에서 전체 게시글 목록을 불러올 때 사용하므로 추가합니다.
    List<Post> findAllByOrderByCreatedAtDesc(); // 👈 이 줄을 추가하는 것을 추천합니다.

    // 특정 회원의 게시글 조회
    List<Post> findByMemberOrderByCreatedAtDesc(Member member);

    // 조회수 순으로 인기 게시글 조회
    List<Post> findTop10ByOrderByViewCountDesc();

    // 내용으로 게시글 검색
    List<Post> findByContentContainingOrderByCreatedAtDesc(String keyword);

    List<Post> findByMember_MemberIdOrderByCreatedAtDesc(Long memberId);
    // 회원별 게시글 수 조회
    @Query("SELECT COUNT(p) FROM Post p WHERE p.member = :member")
    long countByMember(Member member);
}