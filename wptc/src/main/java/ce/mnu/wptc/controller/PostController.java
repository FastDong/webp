package ce.mnu.wptc.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
    private final ReplyRepository replyRepository; // 추가

    @Autowired
    public PostController(PostRepository postRepository, ReplyRepository replyRepository) {
        this.postRepository = postRepository;
        this.replyRepository = replyRepository; // 추가
    }

    @GetMapping("/posts/{id}")
    public String postDetail(@PathVariable Long id, Model model, HttpSession session) {
        Post post = postRepository.findById(id).orElseThrow();
        List<Reply> replies = replyRepository.findByPost_PostId(id);

        // 최상위 댓글만 추출 (parentId == 0)
        List<Reply> topLevelReplies = replies.stream()
            .filter(r -> r.getParentId() == 0)
            .toList();

        // 각 댓글의 자식(대댓글) 맵 생성
        Map<Long, List<Reply>> childReplyMap = replies.stream()
            .filter(r -> r.getParentId() != 0)
            .collect(java.util.stream.Collectors.groupingBy(Reply::getParentId));

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
                             @RequestParam(required = false, defaultValue = "0") Long parentId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Member member = (Member) session.getAttribute("loginMember");
        if (member == null) {
            redirectAttributes.addFlashAttribute("loginError", "로그인이 필요합니다.");
            return "redirect:/posts/" + id;
        }

        // parentId 보정: 만약 parentId가 0이 아니고, parent Reply가 대댓글이면 최상위 댓글로 parentId를 바꿔줌
        if (parentId != 0) {
            Reply parentReply = replyRepository.findById(parentId).orElse(null);
            if (parentReply != null && parentReply.getParentId() != 0) {
                parentId = parentReply.getParentId(); // 최상위 댓글 id로 보정
            }
        }

        Reply reply = new Reply();
        reply.setContents(content);
        reply.setPost(postRepository.findById(id).orElseThrow());
        reply.setMember(member);
        reply.setReplyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        reply.setParentId(parentId); // 항상 최상위 댓글 id만 parentId로 저장
        reply.setLayer(parentId == 0 ? 0 : 1); // 대댓글이면 1, 아니면 0

        replyRepository.save(reply);

        return "redirect:/posts/" + id;
    }


}
