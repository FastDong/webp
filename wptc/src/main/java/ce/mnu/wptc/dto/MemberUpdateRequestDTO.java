package ce.mnu.wptc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequestDTO {
    private String nickname;
    // 비밀번호 변경 등을 위해 다른 필드 추가 가능
}
