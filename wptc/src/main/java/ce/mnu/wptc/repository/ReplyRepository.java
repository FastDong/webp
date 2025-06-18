package ce.mnu.wptc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import ce.mnu.wptc.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByPost_PostId(Long postId);
}
