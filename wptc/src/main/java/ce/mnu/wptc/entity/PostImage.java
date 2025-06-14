package ce.mnu.wptc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "POST_IMAGE")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "post")
@AllArgsConstructor
@Builder
public class PostImage {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_IMAGE_SEQ_GENERATOR")
    @SequenceGenerator(name="POST_IMAGE_SEQ_GENERATOR", sequenceName="POST_IMAGE_SEQ", initialValue=1, allocationSize=1)
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
