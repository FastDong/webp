package ce.mnu.wptc.dto;

import lombok.Getter;
import lombok.Setter;

//수정 후
@Getter @Setter
public class PostCreateRequestDTO {
	 private String title; // 👈 이 필드를 추가합니다.
	 private String content;
}