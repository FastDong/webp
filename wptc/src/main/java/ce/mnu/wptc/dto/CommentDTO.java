package ce.mnu.wptc.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import ce.mnu.wptc.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    
    // Comment 엔티티를 CommentDTO로 변환하는 정적 메서드
    public static CommentDTO fromEntity(Comment comment) {
        return CommentDTO.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .memberId(comment.getMember().getMemberId())
                .memberNickname(comment.getMember().getNickname())
                .content(comment.getContent())
                // 부모 댓글이 없는 최상위 댓글의 경우 Null 처리
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getCommentId() : null)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                // 자식 댓글 목록도 재귀적으로 DTO로 변환
                .childComments(comment.getChildComments().stream()
                        .map(CommentDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
