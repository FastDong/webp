package ce.mnu.wptc.dto;

import ce.mnu.wptc.entity.Member;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
    private Long memberId;
    private String loginId;
    private String password;
    private String nickname;
    private String email;
    private int point;
    private Long gradeId;
    private String gradeName;
    private Boolean emailVerified;
    private LocalDateTime createdAt;
    private int postCount;    // 작성한 게시글 수
    private int commentCount; // 작성한 댓글 수

    public static MemberDTO fromEntity(Member member) {
        return MemberDTO.builder()
                .memberId(member.getMemberId())
                .loginId(member.getLoginId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .point(member.getPoint())
                .gradeId(member.getGrade().getGradeId())
                .gradeName(member.getGrade().getGradeName())
                .emailVerified(member.getEmailVerified())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
