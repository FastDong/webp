package ce.mnu.wptc.controller;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.repository.MemberRepository;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.repository.PostRepository;
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

    @GetMapping("/")
    public String mainPage(Model model, HttpSession session,
                           @RequestParam(defaultValue = "0") int page) {
        Page<Post> postPage = postRepository.findAll(PageRequest.of(page, 3));
        model.addAttribute("postPage", postPage);

        Member member = (Member) session.getAttribute("loginMember");
        model.addAttribute("member", member);
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



}
