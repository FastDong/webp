package ce.mnu.wptc.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ce.mnu.wptc.dto.MemberDTO;
import ce.mnu.wptc.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@ControllerAdvice // 이 클래스가 모든 컨트롤러에 대한 전역 설정을 담당함을 선언
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final MemberService memberService;

    // 이 메서드는 어떤 페이지를 요청하든 컨트롤러가 동작하기 전에 항상 먼저 실행됩니다.
    @ModelAttribute("currentUser") // 여기서 반환되는 값은 "currentUser"라는 이름으로 모든 view(HTML)에 전달됩니다.
    public MemberDTO addCurrentUserToModel(HttpSession session) {
        // 1. 현재 세션에서 "memberId" 속성을 가져옵니다.
        Long memberId = (Long) session.getAttribute("memberId");
        
        // 2. memberId가 존재하면 (로그인 상태이면)
        if (memberId != null) {
        	  try {
                  return memberService.getMemberInfo(memberId);
              } catch (Exception e) {
                  e.printStackTrace(); // 👈 이 코드를 추가해서 에러 내용을 콘솔에 출력
                  return null;
              }
        }
        
        // 4. memberId가 없으면 (로그아웃 상태이면) null을 반환합니다.
        return null;
    }
}