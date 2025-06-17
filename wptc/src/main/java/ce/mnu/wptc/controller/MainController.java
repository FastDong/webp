package ce.mnu.wptc.controller;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.repository.MemberRepository;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String mainPage(Model model, Principal principal) {
        Iterable<Post> iterable = postRepository.findAll();
        List<Post> postList = new ArrayList<>();
        iterable.forEach(postList::add); // 실제 데이터 추가
        model.addAttribute("postList", postList);

        // 로그인한 회원 정보 조회 (로그인 안 했으면 null)
        if (principal != null) {
            String email = principal.getName();
            Member member = memberRepository.findByEmail(email).orElse(null);
            model.addAttribute("member", member);
        }
        return "main";
    }

}
