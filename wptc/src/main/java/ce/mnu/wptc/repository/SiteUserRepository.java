package ce.mnu.wptc.repository;

import org.springframework.data.repository.CrudRepository;

public interface SiteUserRepository extends CrudRepository<SiteUser, Long> {
	SiteUser findByEmail(String email);
}
