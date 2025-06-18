package ce.mnu.wptc.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ce.mnu.wptc.entity.Member;
import ce.mnu.wptc.entity.Post;
import ce.mnu.wptc.entity.Reply;
import ce.mnu.wptc.repository.PostRepository;
import ce.mnu.wptc.repository.ReplyRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;

    @Autowired
    public PostController(PostRepository postRepository, ReplyRepository replyRepository) {
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
    }

    @GetMapping("/posts/{id}")
    public String postDetail(@PathVariable Long id, Model model, HttpSession session) {
        Post post = postRepository.findById(id).orElseThrow();
        List<Reply> replies = replyRepository.findByPost_PostId(id);

        // 최상위 댓글만 추출 (parent == null)
        List<Reply> topLevelReplies = replies.stream()
            .filter(r -> r.getParent() == null)
            .toList();

        // 각 댓글의 자식(대댓글) 맵 생성
        Map<Long, List<Reply>> childReplyMap = replies.stream()
            .filter(r -> r.getParent() != null)
            .collect(Collectors.groupingBy(r -> r.getParent().getReplyId()));

        // 세션에서 로그인한 회원 정보 꺼내서 모델에 추가
        Member member = (Member) session.getAttribute("loginMember");
        model.addAttribute("member", member);

        model.addAttribute("post", post);
        model.addAttribute("topLevelReplies", topLevelReplies);
        model.addAttribute("childReplyMap", childReplyMap);
        return "post_detail";
    }

    @PostMapping("/posts/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             @RequestParam(required = false) Long parent, // parentId → parent
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Member member = (Member) session.getAttribute("loginMember");
        if (member == null) {
            redirectAttributes.addFlashAttribute("loginError", "로그인이 필요합니다.");
            return "redirect:/posts/" + id;
        }

        // parent 보정: 대댓글의 대댓글 방지
        Reply parentReply = null;
        if (parent != null) {
            parentReply = replyRepository.findById(parent).orElse(null);
            if (parentReply != null && parentReply.getParent() != null) {
                // 부모가 이미 대댓글이면, 최상위 댓글로 보정
                parentReply = parentReply.getParent();
            }
        }

        Reply reply = new Reply();
        reply.setContents(content);
        reply.setPost(postRepository.findById(id).orElseThrow());
        reply.setMember(member);
        reply.setReplyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        reply.setParent(parentReply); // 객체 참조로 저장
        reply.setLayer(parentReply == null ? 0 : 1);

        replyRepository.save(reply);

        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{postId}/comment/{replyId}/delete")
    public String deleteReply(@PathVariable Long postId,
                              @PathVariable Long replyId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Member member = (Member) session.getAttribute("loginMember");
        Reply reply = replyRepository.findById(replyId).orElse(null);
        if (reply != null && member != null && reply.getMember().getMemberId().equals(member.getMemberId())) {
            replyRepository.delete(reply); // Cascade 설정 시 자식 대댓글도 함께 삭제
            redirectAttributes.addFlashAttribute("message", "댓글이 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("error", "삭제 권한이 없습니다.");
        }
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/posts/{postId}/comment/{replyId}/edit")
    public String editReply(@PathVariable Long postId, @PathVariable Long replyId,
                            @RequestParam String content, HttpSession session, RedirectAttributes redirectAttributes) {
        Reply reply = replyRepository.findById(replyId).orElseThrow();
        Member member = (Member) session.getAttribute("loginMember");
        if (reply.getMember().getMemberId().equals(member.getMemberId())) {
            reply.setContents(content);
            replyRepository.save(reply);
            redirectAttributes.addFlashAttribute("message", "댓글이 수정되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("error", "수정 권한이 없습니다.");
        }
        return "redirect:/posts/" + postId;
    }
}
