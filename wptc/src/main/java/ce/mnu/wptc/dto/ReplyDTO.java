package ce.mnu.wptc.dto;

import lombok.Data;

@Data
public class ReplyDTO {
    private Long postId;     // 게시글 ID
    private Long memberId;   // 작성자 ID
    private Long parentId = 0L;  // 부모 댓글 ID (기본 0은 루트 댓글)
    private Long layer = 0L;     // 댓글 깊이
    private String contents;     // 댓글 내용
}
