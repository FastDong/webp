package ce.mnu.wptc.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
public class Board {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
    // getter/setter
}
