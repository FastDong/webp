package ce.mnu.wptc.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ce.mnu.wptc.dto.MemberDTO;
import ce.mnu.wptc.dto.MemberJoinRequestDTO;
import ce.mnu.wptc.dto.MemberUpdateRequestDTO;
import ce.mnu.wptc.entity.Grade;
import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.repository.CommentRepository;
import ce.mnu.wptc.repository.GradeRepository;
import ce.mnu.wptc.repository.MemberRepository;
import ce.mnu.wptc.repository.PostRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 주입
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 설정
public class MemberService {

	// MemberService.java
	private final MemberRepository memberRepository;
	private final GradeRepository gradeRepository;
	private final PostRepository postRepository; // 👈 추가
	private final CommentRepository commentRepository; // 👈 추가
	private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional // 데이터 변경이 있으므로 readOnly=false 설정
    public MemberDTO join(MemberJoinRequestDTO requestDto) {
        // 1. 중복 회원 검증
        if (memberRepository.existsByLoginId(requestDto.getLoginId())) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        // 2. 기본 등급 조회 (예: 'BRONZE' 등급, ID가 1L이라고 가정)
        Grade defaultGrade = gradeRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("기본 등급을 찾을 수 없습니다."));

        // 3. 회원 엔티티 생성
        Member newMember = Member.builder()
                .loginId(requestDto.getLoginId())
                .password(passwordEncoder.encode(requestDto.getPassword())) // 실제로는 비밀번호 암호화 필수!
                //.password(requestDto.getPassword()) (임시) 암호화 로직 제외
                .nickname(requestDto.getNickname())
                .email(requestDto.getEmail())
                .grade(defaultGrade)
                .createdAt(LocalDateTime.now())
                .build();

        // 4. 회원 정보 저장
        Member savedMember = memberRepository.save(newMember);

        // 5. DTO로 변환하여 반환
        return MemberDTO.fromEntity(savedMember);
    }

    /**
     * 회원 정보 조회
     */
 // MemberService.java

 // 이 메서드가 호출될 때는 트랜잭션이 활성화된 상태입니다.
	 public MemberDTO getMemberInfo(Long memberId) {
	     // 1. 회원 기본 정보를 찾습니다.
	     Member member = memberRepository.findById(memberId)
	             .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. ID: " + memberId));
	
	     // 2. 이 회원의 게시글 수와 댓글 수를 Repository에서 직접 조회합니다.
	     long postCount = postRepository.countByMember(member);
	     long commentCount = commentRepository.countByMember(member);
	
	     // 3. 모든 정보를 조합하여 DTO를 직접 생성하고 반환합니다.
	     return MemberDTO.builder()
	             .memberId(member.getMemberId())
	             .loginId(member.getLoginId())
	             .nickname(member.getNickname())
	             .email(member.getEmail())
	             .point(member.getPoint())
	             .gradeId(member.getGrade().getGradeId())
	             .gradeName(member.getGrade().getGradeName())
	             .emailVerified(member.getEmailVerified())
	             .createdAt(member.getCreatedAt())
	             .postCount((int) postCount) // long을 int로 캐스팅
	             .commentCount((int) commentCount) // long을 int로 캐스팅
	             .build();
	 }

    /**
     * 회원 정보 수정 (닉네임 변경)
     */
    @Transactional
    public void updateNickname(Long memberId, MemberUpdateRequestDTO requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        member.updateNickname(requestDto.getNickname()); // 주석 해제
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다. ID: " + memberId);
        }
        memberRepository.deleteById(memberId);
    }

    // 엔티티를 DTO로 변환하는 헬퍼 메서드
    private MemberDTO convertToDto(Member member) {
        return MemberDTO.builder()
                .memberId(member.getMemberId())
                .loginId(member.getLoginId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .point(member.getPoint())
                .gradeId(member.getGrade().getGradeId())
                .gradeName(member.getGrade().getGradeName())
                .emailVerified(member.getEmailVerified())
                .createdAt(member.getCreatedAt())
                .build();
    }
    
    public MemberDTO login(String loginId, String rawPassword) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 맞지 않습니다."));

        // 암호화된 비밀번호와 입력된 비밀번호를 비교
        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 맞지 않습니다.");
        }

        // 로그인 성공 시 DTO로 변환하여 반환
        return MemberDTO.fromEntity(member);
    }
}
