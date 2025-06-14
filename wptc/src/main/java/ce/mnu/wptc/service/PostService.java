package ce.mnu.wptc.service;

import java.io.IOException;
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
import ce.mnu.wptc.repository.PostImageRepository;
import ce.mnu.wptc.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
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

    // PostService.java 의 createPost 메서드
    @Transactional
    public PostDTO createPost(PostCreateRequestDTO dto, Long memberId, List<MultipartFile> images) throws IOException {
        
        // 1. 부모(Post) 객체를 생성합니다.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        Post newPost = Post.createPost(member, dto.getTitle(), dto.getContent());

        // 2. 부모를 먼저 저장하고, 즉시 flush()하여 DB에 INSERT 쿼리를 강제로 실행합니다.
        postRepository.save(newPost);
        postRepository.flush(); // ★★★ 이 코드가 핵심입니다 ★★★

        // 3. 이제 newPost.getPostId()는 DB에 의해 확정된 ID를 가집니다.
        //    이 ID를 사용하여 자식(PostImage)들을 저장합니다.
        if (images != null && !images.isEmpty()) {
            int sequence = 1;
            for (MultipartFile imageFile : images) {
                if (imageFile != null && !imageFile.isEmpty()) {
                    String storedImageUrl = fileService.storeFile(imageFile);
                    
                    PostImage postImage = PostImage.builder()
                            .post(newPost) // ID가 확정된 Post를 부모로 설정
                            .imageUrl(storedImageUrl)
                            .sequence(sequence++)
                            .build();

                    postImageRepository.save(postImage);
                }
            }
        }
        
        return PostDTO.fromEntity(newPost);
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
