package ce.mnu.wptc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import ce.mnu.wptc.dto.MemberDTO;
import ce.mnu.wptc.dto.PostSummaryDTO; // 게시글 목록용 DTO (아래에서 정의)
import ce.mnu.wptc.service.MemberService;
import ce.mnu.wptc.service.PostService;   // 게시글 서비스 (아래에서 정의)
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService; // 게시글 데이터를 가져올 서비스
    private final MemberService memberService; // final 키워드 추가
    /**
     * 메인 페이지를 보여주는 메서드
     * @param model View(HTML)에 데이터를 전달하는 객체
     * @return 보여줄 HTML 파일의 이름
     */
    // MainController.java 등에 추가

    /**
     * 모든 모델에 현재 로그인된 사용자 정보를 담아주는 메서드
     */
    @ModelAttribute("currentUser") // "currentUser"라는 이름으로 모델에 담김
    public MemberDTO addCurrentUserToModel(HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        
        if (memberId != null) {
            // 세션에 ID가 있으면, 서비스 계층에서 '최신' 사용자 정보를 가져옴
            try {
                return memberService.getMemberInfo(memberId);
            } catch (IllegalArgumentException e) {
                // 사용자가 삭제되었는데 세션은 남은 경우 등 예외 처리
                return null;
            }
        }
        
        return null; // 비로그인 상태면 null을 모델에 담음
    }
    
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
