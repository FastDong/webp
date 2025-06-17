package ce.mnu.wptc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "REPLY")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long replyId;
	private long postId;
	private long memberId;
	private long parentId;
	private long layer = 0;
	
	private String contents;
	private String replyTime;
	
	public Reply(String contents) {
		this.contents = contents;
	}
}
