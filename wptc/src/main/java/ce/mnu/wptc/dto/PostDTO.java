package ce.mnu.wptc.dto;

import lombok.Data;

@Data
public class PostDTO {
    private String title;
    private String contents;
    private Long memberId; // 작성자 ID
}
