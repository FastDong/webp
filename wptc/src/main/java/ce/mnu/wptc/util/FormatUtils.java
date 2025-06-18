package ce.mnu.wptc.util;

import org.springframework.stereotype.Component;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;

@Component("formatUtils")
public class FormatUtils {

    // ✅ 정확한 단위 값으로 수정한 KOREAN_UNITS 맵
    private static final Map<String, Long> KOREAN_UNITS = new LinkedHashMap<>();
    static {
        KOREAN_UNITS.put("조", 1_0000_0000_0000L); // 10의 12제곱
        KOREAN_UNITS.put("억", 1_0000_0000L);      // 10의 8제곱
        KOREAN_UNITS.put("만", 1_0000L);          // 10의 4제곱
    }

    /**
     * 숫자를 '조', '억', '만', '원' 단위의 한국식 화폐 문자열로 변환합니다.
     * @param number 변환할 숫자
     * @return 포맷팅된 문자열 (예: "191만 1,420원")
     */
    public String formatKoreanWon(long number) {
        if (number == 0) {
            return "0원";
        }

        NumberFormat formatter = NumberFormat.getNumberInstance(); // 천 단위 쉼표 포맷터
        StringBuilder result = new StringBuilder();
        long tempNumber = number;

        // ✅ 가장 큰 단위부터 순서대로 처리하는 개선된 로직
        for (Map.Entry<String, Long> entry : KOREAN_UNITS.entrySet()) {
            String unitText = entry.getKey();
            long unitValue = entry.getValue();

            // 현재 단위로 나눈 몫이 0보다 크면, 해당 단위를 결과에 추가
            long quotient = tempNumber / unitValue;
            if (quotient > 0) {
                result.append(formatter.format(quotient)).append(unitText).append(" ");
                tempNumber %= unitValue; // 나머지 값을 다음 단위 계산에 사용
            }
        }

        // '만' 단위까지 처리하고 남은 나머지 값(1~9999)을 추가
        if (tempNumber > 0) {
            result.append(formatter.format(tempNumber));
        }

        return result.toString().trim() + "원";
    }
}
