package ce.mnu.wptc.entity;

import java.util.ArrayList;
import java.util.List;

import ce.mnu.wptc.repository.BaseTimeEntity; // BaseTimeEntity 경로 확인 필요
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "POSTS") // Oracle 예약어 회피
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "comments", "postImages"})
public class Post extends BaseTimeEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ_GENERATOR")
    @SequenceGenerator(name="POST_SEQ_GENERATOR", sequenceName="POST_SEQ", initialValue=1, allocationSize=1)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    // 👈 BaseTimeEntity에 있으므로 createdAt, updatedAt 필드 삭제

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // 👈 컬렉션 필드 초기화

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>(); // 👈 컬렉션 필드 초기화

    // --- 생성 메서드 ---
    public static Post createPost(Member member, String title, String content) {
        Post post = new Post();
        post.setMember(member);
        post.title = title;
        post.content = content;
        return post;
    }

    // --- 연관관계 편의 메서드 ---
    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }
    
    // --- 비즈니스 로직 메서드 ---
    public void increaseViewCount() {
        this.viewCount++;
    }

    public void updateContent(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    public void addPostImage(PostImage postImage) {
        this.postImages.add(postImage); // 1. 내(Post) 이미지 리스트에 추가
        postImage.setPost(this);      // 2. 이미지(자식)에게 내가 부모임을 설정
    }
}