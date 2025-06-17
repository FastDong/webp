package ce.mnu.wptc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name = "MEMBERS")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;
	private String name;
	@Column(unique = true)
	private String email;
	private String passwd;
	
	private long point = 10000;
	private String rank;
	private long postCount = 0;
	private long replyCount = 0;
	
	public Member(String name, String email, String passwd, long point, String rank, long postCount, long replyCount) {
		this.name = name;
		this.email = email;
		this.passwd = passwd;
		this.point = point;
		this.rank = rank;
		this.postCount = postCount;
		this.replyCount = replyCount;
	}
	
}
