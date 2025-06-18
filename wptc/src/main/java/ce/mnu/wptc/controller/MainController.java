package ce.mnu.wptc.controller;

import ce.mnu.wptc.dto.VirtualStockView;
import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Stocks;
import ce.mnu.wptc.repository.MemberRepository;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.repository.PostRepository;
import ce.mnu.wptc.repository.StocksRepository;
import ce.mnu.wptc.service.StockDataService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final StocksRepository stocksRepository;
    private final StockDataService stockDataService; // ✅ 서비스 주입

    @GetMapping("/")
    public String mainPage(Model model, HttpSession session,
                           @RequestParam(defaultValue = "0") int page) {
        Page<Post> postPage = postRepository.findAll(PageRequest.of(page, 3));
        model.addAttribute("postPage", postPage);

        Member member = (Member) session.getAttribute("loginMember");
        model.addAttribute("member", member);

        // 3. 주식 데이터 로드 (✅ 이 부분 추가!)
        List<Stocks> virtualStocks = stocksRepository.findByMemberIsNull();
        List<VirtualStockView> stockList = new ArrayList<>();

        for (Stocks stock : virtualStocks) {
            VirtualStockView dto = new VirtualStockView();
            dto.setStockId(stock.getStockId());
            dto.setStockName(stock.getStockName());
            dto.setPrice(stock.getPrice());

            long userQuantity = 0;
            if (member != null) {
                // 회원의 보유 수량 조회 로직 (Optional 사용 권장)
                Optional<Stocks> userStockOpt = stocksRepository.findByMemberAndStockName(member, stock.getStockName());
                userQuantity = userStockOpt.map(Stocks::getCount).orElse(0L);
            }
            dto.setUserQuantity(userQuantity);

            // ✅ 여기에 변동 데이터 로직을 추가합니다.
            StockDataService.PriceChangeData changeData = stockDataService.getPriceChange(stock.getStockName());
            dto.setPriceChangeAmount(changeData.amount());
            dto.setPriceChangeRate(changeData.rate());

            stockList.add(dto);
        }
        model.addAttribute("stockList", stockList);

        return "main";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent() && optionalMember.get().getPasswd().equals(password)) {
            session.setAttribute("loginMember", optionalMember.get());
            return "redirect:/";
        } else {
            model.addAttribute("loginError", true);
            return "main";
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
}
