package ce.mnu.wptc.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "login_id", length = 50, nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "nickname", length = 50, nullable = false)
    private String nickname;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "point", nullable = false)
    private int point = 10000; // 기본 포인트 10000

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false; // 기본값 false

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 회원과 게시글의 일대다 관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    // 회원과 댓글의 일대다 관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // 회원과 자산의 일대다 관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAsset> userAssets;

    // 포인트 업데이트 메서드
    public void updatePoint(int newPoint) {
        this.point = newPoint;
    }

    // 등급 업데이트 메서드
    public void updateGrade(Grade newGrade) {
        this.grade = newGrade;
    }

    // 이메일 인증 완료 메서드
    public void verifyEmail() {
        this.emailVerified = true;
    }
}
