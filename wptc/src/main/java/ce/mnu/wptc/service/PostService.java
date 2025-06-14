package ce.mnu.wptc.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ce.mnu.wptc.dto.PostCreateRequestDTO; // 새로 만들어야 할 DTO
import ce.mnu.wptc.dto.PostDTO;
import ce.mnu.wptc.dto.PostSummaryDTO;
import ce.mnu.wptc.dto.PostUpdateRequestDTO; // 새로 만들어야 할 DTO
import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.entity.PostImage;
import ce.mnu.wptc.repository.MemberRepository; // 추가해야 할 의존성
import ce.mnu.wptc.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository; // Member 정보를 가져오기 위해 추가
    private final FileService fileService;

    // 기존의 목록 조회 메서드
    public List<PostSummaryDTO> getPostList() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // --- 여기부터 새로 구현 ---
    @Transactional
    public PostDTO createPost(PostCreateRequestDTO dto, Long memberId, List<MultipartFile> images) throws IOException { // 👈 images 파라미터 추가
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        // 1. 게시글 엔티티부터 생성 (이미지 연결 전)
        Post newPost = Post.builder()
                .member(member)
                .content(dto.getContent())
                // .title(dto.getTitle())
                .build();

        // 2. 이미지 파일들을 저장하고, PostImage 엔티티 리스트를 생성
        List<PostImage> postImages = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            int sequence = 1;
            for (MultipartFile imageFile : images) {
                String storedImageUrl = fileService.storeFile(imageFile);

                PostImage postImage = PostImage.builder()
                        .post(newPost) // PostImage가 어떤 Post에 속하는지 연결
                        .imageUrl(storedImageUrl)
                        .sequence(sequence++)
                        .build();
                postImages.add(postImage);
            }
        }

        // 3. 생성된 PostImage 리스트를 Post 엔티티에 설정
        newPost.setPostImages(postImages);

        // 4. Post 엔티티를 저장 (JPA Cascade 설정에 의해 PostImage도 함께 저장됨)
        Post savedPost = postRepository.save(newPost);

        return PostDTO.fromEntity(savedPost);
    }

    @Transactional
    public PostDTO createPost(PostCreateRequestDTO dto, Long memberId) {
        // 1. 게시글을 작성할 회원을 찾습니다.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        // 2. Post 엔티티를 생성합니다.
        Post newPost = Post.builder()
                .member(member)
                .content(dto.getContent())
                // .title(dto.getTitle()) // Post 엔티티에 title 필드를 추가했다면 이 부분도 포함
                .build();
        
        // 3. 생성한 Post를 저장합니다.
        Post savedPost = postRepository.save(newPost);
        
        // 4. DTO로 변환하여 반환합니다.
        return PostDTO.fromEntity(savedPost);
    }

    @Transactional
    public PostDTO getPostDetails(Long postId) {
        // 1. 게시글을 찾습니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        
        // 2. 조회수를 1 증가시킵니다. (Post 엔티티에 관련 메서드 추가 필요)
        post.increaseViewCount(); // @Transactional에 의해 자동 저장(Dirty Checking)

        // 3. DTO로 변환하여 반환합니다.
        return PostDTO.fromEntity(post);
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequestDTO dto) {
        // 1. 수정할 게시글을 찾습니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        
        // (권한 체크 로직 추가 가능: 현재 로그인한 사용자가 게시글 작성자인지 확인)

        // 2. 내용을 수정합니다. (Post 엔티티에 관련 메서드 추가 필요)
        post.updateContent(dto.getContent());
    }

    @Transactional
    public void deletePost(Long postId) {
        // (권한 체크 로직 추가 가능)
        
        // 1. ID로 게시글을 바로 삭제합니다.
        postRepository.deleteById(postId);
    }
    // PostService.java
    public List<PostSummaryDTO> getPostsByMember(Long memberId) {
        // Member 엔티티를 직접 참조해야 하므로 MemberRepository가 필요하지만,
        // 우선 ID만으로 조회하는 쿼리를 만든다고 가정
        return postRepository.findByMember_MemberIdOrderByCreatedAtDesc(memberId).stream()
                .map(PostSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }
}