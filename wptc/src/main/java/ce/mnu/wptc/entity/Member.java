package ce.mnu.wptc.entity;

import java.util.ArrayList;
import java.util.List;

import ce.mnu.wptc.repository.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "MEMBERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"grade", "posts", "comments", "userAssets"})
public class Member extends BaseTimeEntity { // 👈 1. BaseTimeEntity 상속

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
    private int point = 10000;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;
    
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAsset> userAssets = new ArrayList<>();

    // --- 생성 메서드 (이것만 사용) ---
    // 👈 2. @Builder는 삭제하고, 이 정적 팩토리 메서드만 남깁니다.
    public static Member createMember(String loginId, String password, String nickname, String email, Grade grade) {
        Member member = new Member();
        member.loginId = loginId;
        member.password = password;
        member.nickname = nickname;
        member.email = email;
        member.grade = grade;
        // point와 emailVerified는 필드 기본값으로 자동 초기화됩니다.
        return member;
    }
    
    // --- 비즈니스 로직 메서드 ---
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public void updatePoint(int newPoint) {
        this.point = newPoint;
    }

    public void updateGrade(Grade newGrade) {
        this.grade = newGrade;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }
}