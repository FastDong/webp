package ce.mnu.wptc.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long commentId;
    private Long postId;
    private Long memberId;
    private String memberNickname;
    private String content;
    private Long parentCommentId;
    private List<CommentDTO> childComments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
