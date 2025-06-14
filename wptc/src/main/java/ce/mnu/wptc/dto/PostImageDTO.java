package ce.mnu.wptc.dto;

import ce.mnu.wptc.entity.PostImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImageDTO {
    private Long imageId;
    private Long postId;
    private String imageUrl;
    private int sequence;

 // PostImage 엔티티를 PostImageDTO로 변환하는 정적 메서드
    public static PostImageDTO fromEntity(PostImage postImage) {
        return PostImageDTO.builder()
                .imageId(postImage.getImageId())
                .postId(postImage.getPost().getPostId())
                .imageUrl(postImage.getImageUrl())
                .sequence(postImage.getSequence())
                .build();
    }
}
