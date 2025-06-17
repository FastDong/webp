package ce.mnu.wptc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "POSTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    private String title;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID") // POSTS 테이블의 외래키 이름
    private Member member; // 작성자

    private String postTime;

    private String contents;

    private long viewCount = 0;

    private long replyCount = 0;

    public Post(String title, String contents, Member member) {
        this.title = title;
        this.contents = contents;
        this.member = member;
    }
}
