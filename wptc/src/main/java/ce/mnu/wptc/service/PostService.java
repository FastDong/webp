package ce.mnu.wptc.service;

import ce.mnu.wptc.dto.PostSummaryDTO;
import ce.mnu.wptc.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public List<PostSummaryDTO> getPostList() {
        // 모든 게시글을 최신순으로 조회하여 DTO로 변환한 후 리스트로 반환
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ... 게시글 생성, 상세 조회 등 다른 메서드들 ...
}
