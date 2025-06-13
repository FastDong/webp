package ce.mnu.wptc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// jakarta.validation을 활용한 유효성 검사 어노테이션 추가 가능
// 예: @NotBlank, @Email, @Size(min=8, max=20) 등
@Getter
@Setter
@NoArgsConstructor
public class MemberJoinRequestDTO {
    private String loginId;
    private String password;
    private String nickname;
    private String email;
}
