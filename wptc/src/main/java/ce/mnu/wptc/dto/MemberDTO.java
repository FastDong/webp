package ce.mnu.wptc.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

    private Long memberId;
    private String loginId;
    // 비밀번호는 절대 DTO에 포함시키지 않습니다.
    private String nickname;
    private String email;
    private int point;
    private Long gradeId;
    private String gradeName;
    private Boolean emailVerified;
    private LocalDateTime createdAt;

    // 서비스 계층에서 계산되어 채워지는 필드들
    private int postCount;
    private int commentCount;
    
    // 서비스 계층에서 모든 필드를 채워주므로, fromEntity 정적 메서드는 필요 없습니다.
}
