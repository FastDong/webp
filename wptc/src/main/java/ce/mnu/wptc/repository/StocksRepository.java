package ce.mnu.wptc.repository;

import ce.mnu.wptc.entity.Member;
import org.springframework.data.repository.CrudRepository;

import ce.mnu.wptc.entity.Stocks;

import java.util.List;
import java.util.Optional;


public interface StocksRepository extends CrudRepository<Stocks, Long>{
    List<Stocks> findByMemberIsNull();
    Optional<Stocks> findByMemberAndStockName(Member member, String stockName);

    List<Stocks> findByMember(Member member);
}
