package ce.mnu.wptc.dto;

import lombok.Data;

@Data
public class StocksDTO {
    private String stockName;
    private long count;
    private long price;

    // 추가: 어느 회원이 소유한 주식인지 나타냄
    private long memberId;
}
