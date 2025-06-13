package ce.mnu.wptc.controller;

import ce.mnu.wptc.dto.MemberDTO;
import ce.mnu.wptc.dto.MemberJoinRequestDTO;
import ce.mnu.wptc.dto.MemberUpdateRequestDTO;
import ce.mnu.wptc.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 이 클래스가 REST API 컨트롤러임을 명시
@RequestMapping("/api/members") // 이 컨트롤러의 모든 메서드는 /api/members 경로를 기본으로 가짐
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 API
     */
    @PostMapping("/join")
    public ResponseEntity<MemberDTO> join(@RequestBody MemberJoinRequestDTO requestDTO) {
        MemberDTO newMember = memberService.join(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMember);
    }

    /**
     * 회원 단건 조회 API
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMember(@PathVariable("id") Long memberId) {
        MemberDTO memberInfo = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(memberInfo);
    }

    /**
     * 회원 정보 수정 API (닉네임 변경)
     */
    @PatchMapping("/{id}/nickname")
    public ResponseEntity<Void> updateNickname(@PathVariable("id") Long memberId, @RequestBody MemberUpdateRequestDTO requestDTO) {
        memberService.updateNickname(memberId, requestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 탈퇴 API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("id") Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
