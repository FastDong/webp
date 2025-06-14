package ce.mnu.wptc.config; // 또는 dto 패키지

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ce.mnu.wptc.entity.Member;

public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    // 사용자의 권한을 반환 (지금은 간단히 "ROLE_USER"만 부여)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // 사용자의 암호화된 비밀번호를 반환
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // 사용자의 아이디(loginId)를 반환
    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    // 아래는 계정의 상태를 나타내는 메서드들 (지금은 모두 true로 설정)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}