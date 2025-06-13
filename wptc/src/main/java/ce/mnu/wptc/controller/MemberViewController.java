package ce.mnu.wptc.controller;

import ce.mnu.wptc.dto.MemberJoinRequestDTO;
import ce.mnu.wptc.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members") // 이 컨트롤러는 /members 경로를 기본으로 사용
@RequiredArgsConstructor
public class MemberViewController {

    private final MemberService memberService;

    /**
     * 회원가입 폼(HTML)을 보여주는 메서드
     */
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        // Thymeleaf에서 사용할 수 있도록 빈 DTO 객체를 모델에 담아 전달
        model.addAttribute("memberJoinRequestDTO", new MemberJoinRequestDTO());
        return "signup_input"; // templates/signup.html 파일을 찾아서 보여줌
    }

    /**
     * 회원가입 폼에서 전송된 데이터를 처리하는 메서드
     */
    @PostMapping("/signup")
    public String processSignup(@ModelAttribute MemberJoinRequestDTO requestDto) {
        // 서비스의 join 메서드를 호출하여 회원가입 로직 수행
        memberService.join(requestDto);

        // 회원가입 성공 후 로그인 페이지로 리다이렉트
        return "redirect:/members/login"; // 로그인 페이지 URL로 변경 필요
    }
}
