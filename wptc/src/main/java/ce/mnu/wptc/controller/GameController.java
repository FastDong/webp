package ce.mnu.wptc.controller;

import ce.mnu.wptc.service.GameService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    // 홀짝/업다운 게임 결과에 따라 포인트 지급
    @PostMapping("/point")
    public Map<String, Object> updatePoint(HttpSession session, @RequestBody Map<String, Long> body) {
        long delta = body.getOrDefault("delta", 0L);
        long newPoint = gameService.addPoint(session, delta);

        Map<String, Object> result = new HashMap<>();
        result.put("point", newPoint);
        return result;
    }
}
