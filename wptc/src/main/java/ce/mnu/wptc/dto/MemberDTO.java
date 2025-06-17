package ce.mnu.wptc.dto;

import lombok.Data;

@Data
public class MemberDTO {
	String name;
	String email;
	String passwd;
	long point;
	String rank;
	long postCount;
	long replyCount;
}
