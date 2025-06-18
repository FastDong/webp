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
    private List<Stocks> ownedStockList;
    private long totalStockValue;
}
