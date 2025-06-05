package ce.mnu.wptc.repository;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "site_file")
@Data
public class SiteFile {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long num;
	private String name;
}
