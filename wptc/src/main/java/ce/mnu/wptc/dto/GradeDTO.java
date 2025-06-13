package ce.mnu.wptc.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeDTO {
    private Long gradeId;
    private String gradeName;
    private int requiredPoint;
}
