package ce.mnu.wptc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
	private String email;
	private String passwd;
	private long point = 10000;
	private String rank;
	
	public Member(String name, String email, String passwd, long point, String rank) {
		this.name = name;
		this.email = email;
		this.passwd = passwd;
		this.point = point;
		this.rank = rank;
	}
	
}
