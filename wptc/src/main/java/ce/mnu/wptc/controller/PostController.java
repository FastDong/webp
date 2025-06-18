package ce.mnu.wptc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.entity.Reply;
import ce.mnu.wptc.repository.PostRepository;
import ce.mnu.wptc.repository.ReplyRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository; // 추가

    @Autowired
    public PostController(PostRepository postRepository, ReplyRepository replyRepository) {
        this.postRepository = postRepository;
        this.replyRepository = replyRepository; // 추가
    }

    @GetMapping("/posts/{id}")
    public String postDetail(@PathVariable Long id, Model model, HttpSession session) {
        Post post = postRepository.findById(id).orElseThrow();
        List<Reply> replies = replyRepository.findByPost_PostId(id);

        // 세션에서 로그인한 회원 정보 꺼내서 모델에 추가
        Member member = (Member) session.getAttribute("loginMember");
        model.addAttribute("member", member);

        model.addAttribute("post", post);
        model.addAttribute("replies", replies);
        return "post_detail";
    }
}
