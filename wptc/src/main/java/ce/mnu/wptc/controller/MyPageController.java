package ce.mnu.wptc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import ce.mnu.wptc.dto.PostSummaryDTO;
import ce.mnu.wptc.service.PostService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/my-page")
@RequiredArgsConstructor
public class MyPageController {

    private final PostService postService;

    @GetMapping
    public String myPage(@SessionAttribute(name = "memberId", required = false) Long memberId, Model model) {
        if (memberId == null) {
            return "redirect:/login";
        }
        // 서비스에서 현재 로그인한 유저가 쓴 글 목록을 가져옴
        List<PostSummaryDTO> myPosts = postService.getPostsByMember(memberId);
        model.addAttribute("myPosts", myPosts);
        return "members/my-page"; // templates/members/my-page.html 반환
    }
}