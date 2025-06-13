package ce.mnu.wptc.service;

import ce.mnu.wptc.dto.MemberDTO;
import ce.mnu.wptc.dto.MemberJoinRequestDTO;
import ce.mnu.wptc.dto.MemberUpdateRequestDTO;
import ce.mnu.wptc.entity.Grade;
import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.repository.GradeRepository;
import ce.mnu.wptc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 주입
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 설정
public class MemberService {

    private final MemberRepository memberRepository;
    private final GradeRepository gradeRepository;
    // private final PasswordEncoder passwordEncoder; // Spring Security 사용 시 주입

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
                // .password(passwordEncoder.encode(requestDto.getPassword())) // 실제로는 비밀번호 암호화 필수!
                .password(requestDto.getPassword()) // (임시) 암호화 로직 제외
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
    public MemberDTO getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. ID: " + memberId));
        return MemberDTO.fromEntity(member);
    }

    /**
     * 회원 정보 수정 (닉네임 변경)
     */
    @Transactional
    public void updateNickname(Long memberId, MemberUpdateRequestDTO requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. ID: " + memberId));

        // 엔티티의 비즈니스 메서드를 통해 닉네임 변경
        // member.updateNickname(requestDto.getNickname());
        // @Transactional에 의해 메서드 종료 시 자동으로 DB에 변경 감지(Dirty Checking) 및 업데이트
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
}
