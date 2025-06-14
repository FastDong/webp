package ce.mnu.wptc.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import ce.mnu.wptc.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    private Long postId;
    private Long memberId;
    private String memberNickname;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDTO> comments;
    private List<PostImageDTO> postImages;
    
    // Post 엔티티를 PostDTO로 변환하는 정적 메서드
    public static PostDTO fromEntity(Post post) {
        return PostDTO.builder()
                .postId(post.getPostId())
                .memberId(post.getMember().getMemberId())
                .memberNickname(post.getMember().getNickname())
                //.title(post.getTitle()) // title 필드를 추가했다면 포함
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .comments(post.getComments().stream() // 댓글 목록도 DTO로 변환
                        .map(CommentDTO::fromEntity) 
                        .collect(Collectors.toList()))
                .postImages(post.getPostImages().stream() // 이미지 목록도 DTO로 변환
                        .map(PostImageDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
