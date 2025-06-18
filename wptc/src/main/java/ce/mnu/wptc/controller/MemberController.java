package ce.mnu.wptc.controller;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    // ❌ 로그인 메서드 제거 - Spring Security가 처리
    // @PostMapping("/login") 삭제

    // 회원가입 폼
    @GetMapping("/signup")
    public String signupForm() {
        return "/signup";
    }

    // 회원가입 처리 (비밀번호 암호화)
    @PostMapping("/signup")
    public String signup(@RequestParam String name,
                         @RequestParam String email,
                         @RequestParam String passwd,
                         RedirectAttributes redirectAttributes) {

        long point = 10000;
        String rank = "일반";
        long postCount = 0;
        long replyCount = 0;

        // 이메일 중복 체크
        if(memberRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("emailError", "이미 사용 중인 이메일입니다.");
            return "redirect:/signup";
        }

        // 비밀번호 암호화
        String encodedPw = encoder.encode(passwd);

        Member member = new Member(name, email, encodedPw, point, rank, postCount, replyCount);
        memberRepository.save(member);

        redirectAttributes.addFlashAttribute("signupSuccess", "회원가입을 축하합니다! 🎉");
        return "redirect:/";
    }

    // 나머지 메서드들은 그대로 유지...
    @GetMapping("/findMyId")
    public String findIdForm() {
        return "findMyId";
    }

    @PostMapping("/findMyId")
    public String findId(@RequestParam String email, Model model) {
        boolean exists = memberRepository.findByEmail(email).isPresent();
        model.addAttribute("emailResult", exists);
        return "findMyId";
    }

    @GetMapping("/findMyPw")
    public String findPwForm() {
        return "findMyPw";
    }

    @PostMapping("/findMyPw")
    public String findPw(@RequestParam String email,
                         @RequestParam String name,
                         Model model) {
        Optional<Member> memberOpt = memberRepository.findByEmail(email);
        String pwResult = "";
        if (memberOpt.isPresent() && memberOpt.get().getName().equals(name)) {
            String tempPw = "1234";
            memberOpt.get().setPasswd(encoder.encode(tempPw));
            memberRepository.save(memberOpt.get());
            pwResult = tempPw;
        }
        model.addAttribute("pwResult", pwResult);
        return "findMyPw";
    }
}
