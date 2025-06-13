package ce.mnu.wptc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "POST_IMAGE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl;

    @Column(name = "sequence", nullable = false)
    private int sequence;
}
