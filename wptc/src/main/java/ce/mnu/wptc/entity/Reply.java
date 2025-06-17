package ce.mnu.wptc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "REPLY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyId;

    // 🔽 게시글과의 관계 (POSTS 테이블과 연관)
    @ManyToOne
    @JoinColumn(name = "POST_ID")  // 실제 컬럼명 기준
    private Post post;

    // 🔽 회원과의 관계 (MEMBERS 테이블과 연관)
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    // 부모 댓글 (자기 자신을 참조할 수 있는 구조)
    private long parentId;

    private long layer = 0;

    private String contents;

    private String replyTime;

    // 생성자 예시
    public Reply(String contents) {
        this.contents = contents;
    }
}
