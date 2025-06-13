package ce.mnu.wptc.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "site_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length=20, nullable=false)
	private String name;
	@Column(length=50, nullable=false, unique=true)
	private String email;
	@Column(length=20, nullable=false, name="password")
	private String passwd;


	public SiteUser(String name, String email, String passwd) {
		this.name = name;
		this.email = email;
		this.passwd = passwd;
	}
}
