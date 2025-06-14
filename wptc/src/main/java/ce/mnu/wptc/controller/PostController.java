package ce.mnu.wptc.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ce.mnu.wptc.dto.PostCreateRequestDTO;
import ce.mnu.wptc.dto.PostDTO;
import ce.mnu.wptc.dto.PostSummaryDTO;
import ce.mnu.wptc.dto.PostUpdateRequestDTO;
import ce.mnu.wptc.service.PostService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시글 전체 목록 조회 API
     */
    @GetMapping
    public ResponseEntity<List<PostSummaryDTO>> getAllPosts() {
        List<PostSummaryDTO> posts = postService.getPostList();
        return ResponseEntity.ok(posts);
    }

    /**
     * 게시글 생성 API (이미지 업로드 포함)
     */
    @PostMapping
    public ResponseEntity<PostDTO> createPost(
            @RequestPart("dto") PostCreateRequestDTO requestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        
        // TODO: Spring Security 설정 후, 인증 정보(Principal)에서 현재 로그인한 사용자의 ID를 가져와야 합니다.
        Long currentMemberId = 1L; // 임시로 1번 회원으로 가정

        PostDTO newPost = postService.createPost(requestDTO, currentMemberId, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
    }

    /**
     * 게시글 단건 상세 조회 API
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostDetails(@PathVariable("postId") Long postId) {
        PostDTO post = postService.getPostDetails(postId);
        return ResponseEntity.ok(post);
    }

    /**
     * 게시글 수정 API
     */
    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable("postId") Long postId, @RequestPart("dto") PostUpdateRequestDTO requestDTO) {
        // TODO: 이미지 수정 로직은 별도로 추가 구현이 필요합니다.
        // TODO: 현재 로그인한 사용자가 이 게시글의 작성자인지 확인하는 권한 체크 로직 필요
        postService.updatePost(postId, requestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 삭제 API
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId) {
        // TODO: 현재 로그인한 사용자가 이 게시글의 작성자인지 확인하는 권한 체크 로직 필요
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
