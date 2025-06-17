package ce.mnu.wptc.repository;

import org.springframework.data.repository.CrudRepository;

import ce.mnu.wptc.entity.Member;

public interface MemberRepository extends CrudRepository<Member, Long>{

}
