package ce.mnu.wptc.service;

import ce.mnu.wptc.domain.ArticleDTO;
import ce.mnu.wptc.domain.SiteUserDTO;
import ce.mnu.wptc.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SiteUserService {
	@Autowired
	private SiteUserRepository userRepository;
	@Autowired
	private ArticleRepository articleRepository;

	public Article getArticle(Long num) {
		return articleRepository.getReferenceById(num);
	}

	public Page<ArticleHeader> getArticleHeaders(Pageable pageable) {
		return articleRepository.findArtcleHeaders(pageable);
	}

	public void save(ArticleDTO dto) {
		Article article = new Article();
		article.setAuthor(dto.getAuthor());
		article.setTitle(dto.getTitle());
		article.setContents(dto.getContents());
		articleRepository.save(article);
	}
	public Iterable<Article> getArticleAll() {
		return articleRepository.findAll();
	}

	public void save(SiteUserDTO dto) {
		SiteUser user = new SiteUser(dto.getName(), dto.getEmail(), dto.getPasswd());
		userRepository.save(user);
	}
	public Iterable<SiteUser> getAll() {
		//SELECT * FROM stie_user;
		return userRepository.findAll();
	}
	public SiteUser getEmail(String email) {
		return userRepository.findByEmail(email);
	}
	@Transactional
	public void updateName(String email, String name) {
		SiteUser user = userRepository.findByEmail(email);
		user.setName(name);
	}
}