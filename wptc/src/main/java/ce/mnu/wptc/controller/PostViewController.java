package ce.mnu.wptc.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import ce.mnu.wptc.dto.PostCreateRequestDTO;
import ce.mnu.wptc.dto.PostDTO;
import ce.mnu.wptc.service.PostService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/posts") // /posts 경로로 시작하는 요청을 처리
@RequiredArgsConstructor
public class PostViewController {

    private final PostService postService;

    // 게시글 상세 보기 페이지
    @GetMapping("/{id}")
    public String postDetail(@PathVariable Long id, Model model) {
        // 서비스에서 게시글 상세 정보를 가져옴 (조회수 증가 로직 포함)
        PostDTO post = postService.getPostDetails(id);
        // 모델에 'post'라는 이름으로 담아서 view에 전달
        model.addAttribute("post", post);
        return "posts/detail"; // templates/posts/detail.html 반환
    }
    
    // 새 글 작성 페이지
    @GetMapping("/new")
    public String postNewForm(Model model) {
        model.addAttribute("postRequest", new PostCreateRequestDTO());
        return "posts/form"; // templates/posts/form.html 반환
    }

    @PostMapping("/new")
    public String createPost(@ModelAttribute PostCreateRequestDTO dto, 
                             @RequestParam("images") List<MultipartFile> images, 
                             @SessionAttribute(name = "memberId", required = false) Long memberId) throws IOException {
        if (memberId == null) {
            return "redirect:/login";
        }
        PostDTO newPost = postService.createPost(dto, memberId, images);
        return "redirect:/posts/" + newPost.getPostId();
    }
}