package ce.mnu.wptc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ce.mnu.wptc.dto.MemberJoinRequestDTO;
import ce.mnu.wptc.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/members") // 이 컨트롤러는 /members 경로를 기본으로 사용
@RequiredArgsConstructor
public class MemberViewController {

    private final MemberService memberService;

    /**
     * 회원가입 폼(HTML)을 보여주는 메서드
     */
    // 회원가입 페이지(View)를 보여주는 메서드
    @GetMapping("/signup")
    public String signupForm(Model model) {
        // form과 th:object를 연결해주기 위해 빈 DTO를 모델에 담아 전달
        model.addAttribute("memberJoinRequestDTO", new MemberJoinRequestDTO());
        return "members/signup"; // templates/members/signup.html 반환
    }

    // 회원가입 폼 제출(submit)을 처리하는 메서드
    @PostMapping("/signup")
    public String signup(@ModelAttribute MemberJoinRequestDTO requestDTO) {
        // 서비스 계층에 회원가입 요청을 위임
        memberService.join(requestDTO);
        // 회원가입 성공 후 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }
}
