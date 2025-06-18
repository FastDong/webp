package ce.mnu.wptc.controller;

import ce.mnu.wptc.dto.VirtualStockView;
import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Stocks;
import ce.mnu.wptc.repository.MemberRepository;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.repository.PostRepository;
import ce.mnu.wptc.repository.StocksRepository;
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
    @GetMapping("/logout")
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

    @GetMapping("/stocks")
    public String stockTab(Model model, HttpSession session) {
        // 1. 세션에서 로그인한 회원 정보 꺼내기
        Member member = (Member) session.getAttribute("loginMember");

        // 2. 5개 가상주식(공용) 조회
        List<Stocks> virtualStocks = stocksRepository.findByMemberIsNull();
        List<VirtualStockView> stockList = new ArrayList<>();

        for (Stocks stock : virtualStocks) {
            VirtualStockView dto = new VirtualStockView();
            dto.setStockId(stock.getStockId());
            dto.setStockName(stock.getStockName());
            dto.setPrice(stock.getPrice());

            // 3. 로그인한 회원이면 보유수량 조회, 아니면 0
            long userQuantity = 0;
            if (member != null) {
                Optional<Stocks> userStockOpt = stocksRepository.findByMemberAndStockName(member, stock.getStockName());
                userQuantity = userStockOpt.map(Stocks::getCount).orElse(0L);
            }
            dto.setUserQuantity(userQuantity);
            stockList.add(dto);
        }
        model.addAttribute("stockList", stockList);
        model.addAttribute("member", member); // 필요시 추가

        return "stocks";
    }




}
