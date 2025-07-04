package ce.mnu.wptc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import ce.mnu.wptc.entity.Post;


public interface PostRepository extends JpaRepository<Post, Long>{
	 Page<Post> findAll(Pageable pageable);
	 
}
