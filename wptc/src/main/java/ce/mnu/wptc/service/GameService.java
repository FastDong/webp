package ce.mnu.wptc.service;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final MemberRepository memberRepository;

    @Transactional
    public long addPoint(HttpSession session, long delta) {
        Member sessionMember = (Member) session.getAttribute("loginMember");
        if (sessionMember == null) throw new RuntimeException("로그인 필요");

        // DB에서 최신 멤버 조회 (영속성 보장)
        Member dbMember = memberRepository.findById(sessionMember.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원 정보 없음"));

        dbMember.setPoint(dbMember.getPoint() + delta);
        memberRepository.save(dbMember);

        // 세션 정보도 최신화
        session.setAttribute("loginMember", dbMember);

        return dbMember.getPoint();
    }
}
