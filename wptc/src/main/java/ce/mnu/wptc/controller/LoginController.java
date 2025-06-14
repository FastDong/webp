package ce.mnu.wptc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // 로그인 폼 페이지만 보여주는 역할만 남김
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // POST 방식의 loginProcess 메서드는 완전히 삭제합니다.
}