package ce.mnu.wptc.controller;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MemberController {

    private final MemberRepository memberRepository;

    // 회원가입 폼 보여주기
    @GetMapping("/signup")
    public String signupForm() {
        return "/signup"; // templates/members/signup.html
    }

    // 회원가입 처리 (폼에서 전송된 데이터 받기)
    @PostMapping("/signup")
    public String signup(@RequestParam String name,
                         @RequestParam String email,
                         @RequestParam String passwd,
                         RedirectAttributes redirectAttributes) {

        // point와 rank는 기본값으로 설정
        long point = 10000;
        String rank = "일반";
        long postCount = 0;
        long replyCount = 0;


        // 이메일 중복 체크
        if(memberRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("emailError", "이미 사용 중인 이메일입니다.");
            return "redirect:/signup";
        }


        // Member 객체 생성 및 저장
        Member member = new Member(name, email, passwd, point, rank, postCount, replyCount);
        memberRepository.save(member);

        // 성공 메시지 추가
        redirectAttributes.addFlashAttribute("signupSuccess", "회원가입을 축하합니다! 🎉");

        // 회원가입 후 메인 페이지로 리다이렉트
        return "redirect:/";
    }
}
