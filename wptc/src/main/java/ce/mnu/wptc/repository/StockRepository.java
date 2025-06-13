package ce.mnu.wptc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // 주식 코드로 조회
    Optional<Stock> findByStockCode(String stockCode);

    // 주식명으로 검색
    Optional<Stock> findByStockName(String stockName);

    // 주식 코드 존재 여부 확인
    boolean existsByStockCode(String stockCode);
}
