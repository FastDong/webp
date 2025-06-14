package ce.mnu.wptc.repository;

import ce.mnu.wptc.entity.Comment;
import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 최상위 댓글만 조회 (대댓글 제외)
    List<Comment> findByPostAndParentCommentIsNullOrderByCreatedAtAsc(Post post);

    // 특정 댓글의 대댓글 조회
    List<Comment> findByParentCommentOrderByCreatedAtAsc(Comment parentComment);

    // 특정 회원의 댓글 수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.member = :member")
    long countByMember(Member member);
}
