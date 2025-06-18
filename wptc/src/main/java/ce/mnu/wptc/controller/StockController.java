package ce.mnu.wptc.controller;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.service.StockService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/buy")
    public String buyStock(@RequestParam Long stockId,
                           @RequestParam long quantity,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        Member member = (Member) session.getAttribute("loginMember");
        if (member == null) {
            redirectAttributes.addFlashAttribute("tradeError", "로그인이 필요합니다.");
            return "redirect:/";
        }

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
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        Member member = (Member) session.getAttribute("loginMember");
        if (member == null) {
            redirectAttributes.addFlashAttribute("tradeError", "로그인이 필요합니다.");
            return "redirect:/";
        }

        try {
            stockService.sellStock(member.getMemberId(), stockId, quantity);
            redirectAttributes.addFlashAttribute("tradeSuccess", "매도가 완료되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("tradeError", e.getMessage());
        }

        return "redirect:/";
    }
}
