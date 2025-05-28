package ce.mnu.wptc.service;

import ce.mnu.wptc.repository.BoardRepository;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    // 생성자 주입
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
    // 글 저장, 조회 등 메서드 작성
}
