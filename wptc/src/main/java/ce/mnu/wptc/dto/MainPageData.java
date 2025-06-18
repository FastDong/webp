package ce.mnu.wptc.dto;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.entity.Stocks;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import java.util.List;

@Data
@Builder // 빌더 패턴을 사용해 객체 생성을 용이하게 합니다.
public class MainPageData {
    private Page<Post> postPage;
    private Member member;
    private List<VirtualStockView> stockList;
    private List<OwnedStockView> ownedStockViewList;
    private long totalStockValue;

    // ✅ 아래 3개 필드를 새로 추가합니다.
    private final long totalPurchasePrice; // 총 매수 금액
    private final long totalProfitLoss;    // 총 평가 손익
    private final double totalProfitLossRate;  // 총 평가 수익률
}
