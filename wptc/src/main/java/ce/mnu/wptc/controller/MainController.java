package ce.mnu.wptc.controller;

import ce.mnu.wptc.dto.MainPageData;
import ce.mnu.wptc.dto.VirtualStockView;
import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.repository.MemberRepository;
import ce.mnu.wptc.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainPageService mainPageService;
    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String mainPage(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String error) {

        // 1. Spring Security에서 인증된 사용자 정보 가져오기
        Long memberId = getCurrentMemberId();

        // 2. 페이징 정보 생성
        Pageable pageable = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "postId"));

        // 3. 서비스 호출하여 메인 페이지 데이터 가져오기
        MainPageData mainPageData = mainPageService.prepareMainPageData(memberId, pageable);

        // 4. 모델에 데이터 추가
        model.addAttribute("postPage", mainPageData.getPostPage());
        model.addAttribute("member", mainPageData.getMember());
        model.addAttribute("stockList", mainPageData.getStockList());
        model.addAttribute("ownedStockViewList", mainPageData.getOwnedStockViewList());
        model.addAttribute("totalStockValue", mainPageData.getTotalStockValue());
        model.addAttribute("totalProfitLoss", mainPageData.getTotalProfitLoss());
        model.addAttribute("totalProfitLossRate", mainPageData.getTotalProfitLossRate());

        // 5. 로그인 에러 처리 개선
        if ("true".equals(error)) {
            model.addAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return "main";
    }

    @GetMapping("/api/stocks")
    @ResponseBody
    public List<VirtualStockView> getRealTimeStocks() {
        Long memberId = getCurrentMemberId();
        return mainPageService.getRealTimeStockViews(memberId);
    }

    /**
     * 현재 로그인한 사용자의 Member ID를 반환
     * 성능 최적화: Optional 체이닝 사용
     */
    private Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getName())) {
            return null;
        }

        String email = authentication.getName();
        return memberRepository.findByEmail(email)
                .map(Member::getMemberId)
                .orElse(null);
    }

    /**
     * 현재 로그인한 사용자의 Member 객체를 반환 (필요시 사용)
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
