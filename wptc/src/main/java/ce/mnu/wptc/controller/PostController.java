package ce.mnu.wptc.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.repository.PostRepository;
import jakarta.servlet.http.HttpSession;


@Controller
public class PostController {
    private final PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/posts/{id}")
    public String postDetail(@PathVariable("id") Long id, Model model, HttpSession session) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            // 게시글이 없으면 에러 페이지로 이동
            return "error/404";
        }
        Post post = optionalPost.get();
        model.addAttribute("post", post);

        // 로그인 정보도 필요하다면
        Member member = (Member) session.getAttribute("loginMember");
        model.addAttribute("member", member);

        // 댓글 등 추가 데이터 필요시 model에 추가
        return "post_detail";
    }
}

