package ce.mnu.wptc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateRequestDTO {
    // 닉네임 변경에 필요한 필드만 포함
    private String nickname;
}