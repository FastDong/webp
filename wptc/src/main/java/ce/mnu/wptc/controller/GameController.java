package ce.mnu.wptc.controller;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.repository.MemberRepository;
import ce.mnu.wptc.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final MemberRepository memberRepository;

    // 홀짝/업다운 게임 결과에 따라 포인트 지급
    @PostMapping("/point")
    public Map<String, Object> updatePoint(@RequestBody Map<String, Long> body) {
        long delta = body.getOrDefault("delta", 0L);

        Optional<Member> memberOpt = getCurrentMember();
        Map<String, Object> result = new HashMap<>();

        if (memberOpt.isEmpty()) {
            result.put("error", "로그인이 필요합니다.");
            return result;
        }

        Member member = memberOpt.get();
        long newPoint = gameService.addPoint(member.getMemberId(), delta);
        result.put("point", newPoint);
        return result;
    }

    /**
     * 현재 로그인한 사용자의 Member 객체를 반환
     */
    private Optional<Member> getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
            "anonymousUser".equals(authentication.getName())) {
            return Optional.empty();
        }

        String email = authentication.getName();
        return memberRepository.findByEmail(email);
    }
}
