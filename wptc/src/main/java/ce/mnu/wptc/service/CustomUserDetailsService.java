package ce.mnu.wptc.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ce.mnu.wptc.config.CustomUserDetails;
import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 로그인 폼에서 입력한 username(우리의 loginId)으로 DB에서 회원을 찾음
        Member member = memberRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        // 찾은 회원 정보를 Spring Security가 사용하는 UserDetails 형태로 감싸서 반환
        return new CustomUserDetails(member);
    }
}