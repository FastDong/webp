package ce.mnu.wptc.service;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final MemberRepository memberRepository;

    @Transactional
    public long addPoint(long memberId, long delta) {
        // DB에서 최신 멤버 조회 (영속성 보장)
        Member dbMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 정보 없음"));

        dbMember.setPoint(dbMember.getPoint() + delta);
        memberRepository.save(dbMember);

        return dbMember.getPoint();
    }

    /**
     * 현재 로그인한 사용자의 Member 객체를 반환
     */
    public Optional<Member> getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
            "anonymousUser".equals(authentication.getName())) {
            return Optional.empty();
        }

        String email = authentication.getName();
        return memberRepository.findByEmail(email);
    }
}
