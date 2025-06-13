package ce.mnu.wptc.dto;

import ce.mnu.wptc.entity.Post;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PostSummaryDTO {
    private Long postId;
    private String authorNickname;
    private String title; // 게시글 제목 (여기서는 content 일부를 사용)
    private LocalDateTime createdAt;
    private int viewCount;
    private int commentCount;

    public static PostSummaryDTO fromEntity(Post post) {
        // 실제로는 Post 엔티티에 title 필드가 있어야 하지만,
        // 여기서는 content의 앞부분을 잘라 제목처럼 사용합니다.
        String title = post.getContent();
        if (title != null && title.length() > 30) {
            title = title.substring(0, 30) + "...";
        }

        return PostSummaryDTO.builder()
                .postId(post.getPostId())
                .authorNickname(post.getMember().getNickname())
                .title(title)
                .createdAt(post.getCreatedAt())
                .viewCount(post.getViewCount())
                .commentCount(post.getComments() != null ? post.getComments().size() : 0)
                .build();
    }
}
