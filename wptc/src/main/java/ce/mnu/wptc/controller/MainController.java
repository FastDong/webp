package ce.mnu.wptc.controller;

import ce.mnu.wptc.dto.MainPageData;
import ce.mnu.wptc.dto.VirtualStockView;
import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.repository.MemberRepository;
import ce.mnu.wptc.service.MainPageService;
import ce.mnu.wptc.service.MemberService;
import ce.mnu.wptc.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainPageService mainPageService;
    private final MemberRepository memberRepository;
    private final PostService postService;
    private final MemberService memberService;
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


    @GetMapping("/post_make")
    public String postMakeForm(Model model) {
        // [기존] 세션에서 직접 꺼내기
        // Member member = (Member) session.getAttribute("loginMember");

        // [수정] SecurityContext에서 회원 정보 가져오기
        Optional<Member> memberOpt = getCurrentMember();
        memberOpt.ifPresent(member -> model.addAttribute("member", member));
        return "post_make";
    }

    @PostMapping("/posts/new")
    public String createPost(@RequestParam String title,
                             @RequestParam String content,
                             RedirectAttributes redirectAttributes) { // HttpSession 매개변수 제거

        // [기존] 세션에서 loginMember 조회
        // Member loginMember = (Member) session.getAttribute("loginMember");

        // [수정] SecurityContext 사용
        Optional<Member> memberOpt = getCurrentMember();
        if (memberOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/";
        }

        try {
            postService.createPost(memberOpt.get(), title, content);
            redirectAttributes.addFlashAttribute("success", "게시글이 등록되었습니다!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "게시글 등록에 실패했습니다.");
        }
        return "redirect:/";
    }

    // 수정 폼 보여주기 (GET)
    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model, Principal principal) {
        Post post = postService.findById(id);
        Member loginMember = memberService.findByEmail(principal.getName());
        if (!post.getMember().getMemberId().equals(loginMember.getMemberId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }
        model.addAttribute("post", post);
        model.addAttribute("member", loginMember);
        return "post_update";
    }

    // 수정 처리 (POST)
    @PostMapping("/posts/{id}/edit")
    public String editPost(@PathVariable Long id,
                           @RequestParam String title,
                           @RequestParam String content,
                           Principal principal,
                           RedirectAttributes redirectAttributes) {
        Post post = postService.findById(id);
        Member loginMember = memberService.findByEmail(principal.getName());
        if (!post.getMember().getMemberId().equals(loginMember.getMemberId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }
        postService.updatePost(id, title, content);
        redirectAttributes.addFlashAttribute("success", "게시글이 수정되었습니다.");
        return "redirect:/posts/" + id;
    }


}
