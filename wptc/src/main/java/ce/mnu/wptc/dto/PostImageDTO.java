package ce.mnu.wptc.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImageDTO {
    private Long imageId;
    private Long postId;
    private String imageUrl;
    private int sequence;
}
