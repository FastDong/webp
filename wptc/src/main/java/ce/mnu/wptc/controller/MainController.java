package ce.mnu.wptc.controller;

import ce.mnu.wptc.dto.PostSummaryDTO; // 게시글 목록용 DTO (아래에서 정의)
import ce.mnu.wptc.service.PostService;   // 게시글 서비스 (아래에서 정의)
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService; // 게시글 데이터를 가져올 서비스

    /**
     * 메인 페이지를 보여주는 메서드
     * @param model View(HTML)에 데이터를 전달하는 객체
     * @return 보여줄 HTML 파일의 이름
     */
    @GetMapping("/") // 웹사이트의 루트 경로("/") 요청을 처리
    public String mainPage(Model model) {
        // 1. 서비스에게 게시글 목록 데이터를 요청
        List<PostSummaryDTO> posts = postService.getPostList();

        // 2. 받아온 데이터를 "posts"라는 이름으로 모델에 담는다.
        //    이제 HTML에서 "posts"라는 이름으로 이 데이터 목록을 사용할 수 있다.
        model.addAttribute("posts", posts);

        // 3. resources/templates/main.html 파일을 찾아서 사용자에게 보여준다.
        return "main";
    }
}
