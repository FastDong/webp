package ce.mnu.wptc.service;

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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;
	private final GradeRepository gradeRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
	public MemberDTO join(MemberJoinRequestDTO requestDto) {
	    // 중복 회원 검증
        if (memberRepository.existsByLoginId(requestDto.getLoginId())) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }
        
	    // 기본 등급 조회
	    Grade defaultGrade = gradeRepository.findById(1L)
	            .orElseThrow(() -> new IllegalStateException("기본 등급을 찾을 수 없습니다."));

	    // 정적 팩토리 메서드로 Member 엔티티 생성
	    Member newMember = Member.createMember(
	            requestDto.getLoginId(),
	            passwordEncoder.encode(requestDto.getPassword()),
	            requestDto.getNickname(),
	            requestDto.getEmail(),
	            defaultGrade
	    );

	    Member savedMember = memberRepository.save(newMember);

        // 생성된 사용자의 완전한 정보를 DTO로 변환하여 반환
	    return getMemberInfo(savedMember.getMemberId());
	}

    /**
     * 로그인
     */
    public MemberDTO login(String loginId, String rawPassword) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 맞지 않습니다."));

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 맞지 않습니다.");
        }

        // 로그인 성공 시, 게시글/댓글 수를 포함한 완전한 DTO를 반환
        return getMemberInfo(member.getMemberId());
    }

    /**
     * 회원 정보 조회 (게시글/댓글 수 포함)
     */
	 public MemberDTO getMemberInfo(Long memberId) {
	     Member member = memberRepository.findById(memberId)
	             .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. ID: " + memberId));
	
	     long postCount = postRepository.countByMember(member);
	     long commentCount = commentRepository.countByMember(member);
	
	     // DTO 생성 로직을 이 메서드로 일원화
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
	             .postCount((int) postCount)
	             .commentCount((int) commentCount)
	             .build();
	 }

    /**
     * 회원 정보 수정 (닉네임 변경)
     */
    @Transactional
    public void updateNickname(Long memberId, MemberUpdateRequestDTO requestDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        member.updateNickname(requestDTO.getNickname());
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
}
