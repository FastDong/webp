package ce.mnu.wptc.repository;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long num;
	@Column(length=20, nullable = false)
	private String author;
	@Column(length=50, nullable = false)
	private String title;
	@Column(length=2048, nullable = false)
	private String contents;
}
