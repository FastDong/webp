package ce.mnu.wptc.controller;

import ce.mnu.wptc.dto.MainPageData;
import ce.mnu.wptc.dto.VirtualStockView;
import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Stocks;
import ce.mnu.wptc.repository.MemberRepository;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.repository.PostRepository;
import ce.mnu.wptc.repository.StocksRepository;
import ce.mnu.wptc.service.MainPageService;
import ce.mnu.wptc.service.StockDataService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainPageService mainPageService; // ✅ 서비스만 주입받습니다.
    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String mainPage(Model model, HttpSession session,
                           @RequestParam(defaultValue = "0") int page) {

        // 1. 세션에서 로그인한 회원의 ID 가져오기
        Member sessionMember = (Member) session.getAttribute("loginMember");
        Long memberId = (sessionMember != null) ? sessionMember.getMemberId() : null;

        // 2. 페이징 정보 생성
        Pageable pageable = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "postId"));

        // 3. 서비스 호출하여 메인 페이지에 필요한 모든 데이터를 한번에 가져오기
        MainPageData mainPageData = mainPageService.prepareMainPageData(memberId, pageable);

        // 4. 모델에 데이터 추가
        model.addAttribute("postPage", mainPageData.getPostPage());
        model.addAttribute("member", mainPageData.getMember());
        model.addAttribute("stockList", mainPageData.getStockList());
        model.addAttribute("ownedStockViewList", mainPageData.getOwnedStockViewList());
        model.addAttribute("totalStockValue", mainPageData.getTotalStockValue());
        // ✅ 아래 2개 속성을 모델에 새로 추가합니다.
        model.addAttribute("totalProfitLoss", mainPageData.getTotalProfitLoss());
        model.addAttribute("totalProfitLossRate", mainPageData.getTotalProfitLossRate());

        return "main";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent() && optionalMember.get().getPasswd().equals(password)) {
            session.setAttribute("loginMember", optionalMember.get());
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "redirect:/?error";
        }
    }


    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/some-protected-page")
    public String protectedPage(HttpSession session) {
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/"; // 로그인 안 했으면 메인으로
        }
        // 로그인 되어 있으면 페이지 반환
        return "protected";
    }

    @GetMapping("/api/stocks")
    @ResponseBody
    public List<VirtualStockView> getRealTimeStocks(HttpSession session) {
        // 1. 세션에서 로그인한 회원의 ID 가져오기
        Member sessionMember = (Member) session.getAttribute("loginMember");
        Long memberId = (sessionMember != null) ? sessionMember.getMemberId() : null;

        // 2. 서비스 호출하여 DTO 리스트를 바로 반환
        return mainPageService.getRealTimeStockViews(memberId);
    }

}
