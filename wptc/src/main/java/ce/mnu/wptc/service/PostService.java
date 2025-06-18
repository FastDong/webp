package ce.mnu.wptc.service;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void createPost(Member member, String title, String content) {
        Post post = new Post();
        post.setMember(member);
        post.setTitle(title);
        post.setContents(content);
        post.setPostTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        post.setReplyCount(0);
        post.setViewCount(0);

        postRepository.save(post);
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
    }

    // 삭제 기능 (추가)
    @Transactional
    public void delete(Long id) {
        Post post = findById(id);
        postRepository.delete(post);
    }

    @Transactional
    public void updatePost(Long postId, String title, String content) {
        // 1. 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 2. 값 변경
        post.setTitle(title);
        post.setContents(content); // 필드명이 content 또는 contents에 맞게!

        // 3. JPA의 Dirty Checking으로 트랜잭션 끝나면 자동 저장
    }
}
