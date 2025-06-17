package ce.mnu.wptc.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "POSTS")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long postId;
	private String title;
	private String memberName;
	
	private String postTime;
	private String contents;
	private long viewCount = 0;
	private long replyCount = 0;
	
	public Post(String title, String contents, String memberName) {
		this.title = title;
		this.contents = contents;
		this.memberName = memberName;
	}
}