package ce.mnu.wptc;

import ce.mnu.wptc.service.BoardService;
import org.springframework.stereotype.Controller;

@Controller
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 생성자 주입
    // 글 목록, 글 작성 등의 요청을 처리하는 메서드 작성
}