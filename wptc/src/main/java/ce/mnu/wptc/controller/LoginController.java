package ce.mnu.wptc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Model 추가
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ce.mnu.wptc.dto.MemberDTO;
import ce.mnu.wptc.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    // 1. 로그인 폼 페이지를 보여주는 메서드 (변경 없음)
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // 2. HTML <form>의 제출을 처리하고, 성공 시 login_done 페이지를 보여주는 메서드
    @PostMapping("/login")
    public String loginProcess(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) { // Model 객체 추가
    	// LoginController.java - loginProcess 메서드
    	try {
    	    // 서비스 호출하여 로그인 시도
    	    MemberDTO member = memberService.login(username, password);
    	    
    	    // 성공 시 세션에 정보 저장
    	    session.setAttribute("memberId", member.getMemberId());
    	    
    	    // Model에 데이터를 담을 필요 없이, 바로 메인 페이지("/")로 리다이렉트
    	    return "redirect:/";

    	} catch (IllegalArgumentException e) {
    	    // 로그인 실패 시, 에러 파라미터와 함께 다시 로그인 페이지로 리다이렉트
    	    return "redirect:/login?error";
    	}
    }
}