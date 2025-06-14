package ce.mnu.wptc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.entity.PostImage;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    // 특정 게시글의 이미지들을 순서대로 조회
    List<PostImage> findByPostOrderBySequenceAsc(Post post);

    // 특정 게시글의 이미지 삭제
    void deleteByPost(Post post);
}
