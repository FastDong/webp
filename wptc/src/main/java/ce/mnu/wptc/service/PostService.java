package ce.mnu.wptc.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ce.mnu.wptc.dto.PostCreateRequestDTO;
import ce.mnu.wptc.dto.PostDTO;
import ce.mnu.wptc.dto.PostSummaryDTO;
import ce.mnu.wptc.dto.PostUpdateRequestDTO;
import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.entity.PostImage;
import ce.mnu.wptc.repository.CommentRepository;
import ce.mnu.wptc.repository.MemberRepository;
import ce.mnu.wptc.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository; // 의존성 추가
    private final FileService fileService;

    // 게시글 목록 조회
    public List<PostSummaryDTO> getPostList() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 사용자가 쓴 글 목록 조회
    public List<PostSummaryDTO> getPostsByMember(Long memberId) {
        return postRepository.findByMember_MemberIdOrderByCreatedAtDesc(memberId).stream()
                .map(PostSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // (수정) 게시글 생성 (이미지 처리 포함, 메서드 통합)
    @Transactional
    public PostDTO createPost(PostCreateRequestDTO dto, Long memberId, List<MultipartFile> images) throws IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        // 👈 빌더 대신 우리가 만든 정적 팩토리 메서드를 사용
        Post newPost = Post.createPost(member, dto.getTitle(), dto.getContent());

        // 이미지 파일 처리
        if (images != null && !images.isEmpty()) {
            List<PostImage> postImages = new ArrayList<>();
            int sequence = 1;
            for (MultipartFile imageFile : images) {
                if (imageFile != null && !imageFile.isEmpty()) {
                    String storedImageUrl = fileService.storeFile(imageFile);
                    PostImage postImage = PostImage.builder()
                            .post(newPost)
                            .imageUrl(storedImageUrl)
                            .sequence(sequence++)
                            .build();
                    postImages.add(postImage);
                }
            }
            newPost.setPostImages(postImages);
        }

        // Post를 저장하면 연관된 PostImage도 함께 저장됨 (Cascade 설정 덕분)
        Post savedPost = postRepository.save(newPost);
        return PostDTO.fromEntity(savedPost);
    }

    // 게시글 상세 조회 (조회수 증가)
    @Transactional
    public PostDTO getPostDetails(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        
        post.increaseViewCount();
        return PostDTO.fromEntity(post);
    }

    // (수정) 게시글 수정
    @Transactional
    public void updatePost(Long postId, PostUpdateRequestDTO dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        
        // (권한 체크 로직 추가 필요)

        // 👈 엔티티에 만들어둔 수정 메서드를 사용 (title도 함께 수정)
        post.updateContent(dto.getTitle(), dto.getContent());
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId) {
        // (권한 체크 로직 추가 필요)
        postRepository.deleteById(postId);
    }
}
