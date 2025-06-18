package ce.mnu.wptc.controller;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.repository.MemberRepository;
import ce.mnu.wptc.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final MemberRepository memberRepository;

    @PostMapping("/buy")
    public String buyStock(@RequestParam Long stockId,
                           @RequestParam long quantity,
                           RedirectAttributes redirectAttributes) {

        // Spring Security에서 현재 로그인한 사용자 정보 가져오기
        Optional<Member> memberOpt = getCurrentMember();
        if (memberOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("tradeError", "로그인이 필요합니다.");
            return "redirect:/";
        }

        Member member = memberOpt.get();

        try {
            stockService.buyStock(member.getMemberId(), stockId, quantity);
            redirectAttributes.addFlashAttribute("tradeSuccess", "매수가 완료되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("tradeError", e.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/sell")
    public String sellStock(@RequestParam Long stockId,
                            @RequestParam long quantity,
                            RedirectAttributes redirectAttributes) {

        Optional<Member> memberOpt = getCurrentMember();
        if (memberOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("tradeError", "로그인이 필요합니다.");
            return "redirect:/";
        }

        Member member = memberOpt.get();

        try {
            stockService.sellStock(member.getMemberId(), stockId, quantity);
            redirectAttributes.addFlashAttribute("tradeSuccess", "매도가 완료되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("tradeError", e.getMessage());
        }

        return "redirect:/";
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
