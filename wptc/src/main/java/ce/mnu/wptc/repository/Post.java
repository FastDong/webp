package ce.mnu.wptc.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ValueGenerationType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="POST")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long postId;
	
	// POST(N) : MEMBER(1) 관계
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id",nullable =false)
	private Member member;
	
	@Lob
	@Column( name = "content")
	private String content;
	
	@Column(name = "view_count", nullable = false)
	private int viewCount;
	
	// POST(1) : POST_IMAGE(N), COMMENTS(N) 관계, 케스케이딩
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostImage> postImages = new ArrayList<>();
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
	
}
