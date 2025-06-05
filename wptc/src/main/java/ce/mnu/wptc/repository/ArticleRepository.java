package ce.mnu.wptc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	@Query(value="SELECT num, title, author FROM article", nativeQuery = true)
	Page<ArticleHeader> findArtcleHeaders(Pageable pageable);
}
