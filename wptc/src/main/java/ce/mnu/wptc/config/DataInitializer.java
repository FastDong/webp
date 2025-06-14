package ce.mnu.wptc.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ce.mnu.wptc.entity.Grade;
import ce.mnu.wptc.repository.GradeRepository;
import lombok.RequiredArgsConstructor;

@Component // Spring이 이 클래스를 Bean으로 등록하여 관리하도록 함
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final GradeRepository gradeRepository;

    @Override
    @Transactional // 데이터 변경이 있으므로 트랜잭션 처리
    public void run(ApplicationArguments args) throws Exception {
        // 애플리케이션 시작 시점에 이 코드가 딱 한 번 실행됩니다.
        
        // DB에 등급 데이터가 이미 있는지 확인하여, 중복 생성을 방지합니다.
        if (gradeRepository.count() == 0) {
            
            // 데이터가 없을 때만 초기 등급 생성
            Grade bronze = Grade.builder()
                                .gradeName("BRONZE")
                                .requiredPoint(0)
                                .build();
            
            Grade silver = Grade.builder()
                                .gradeName("SILVER")
                                .requiredPoint(10000)
                                .build();

            Grade gold = Grade.builder()
                               .gradeName("GOLD")
                               .requiredPoint(50000)
                               .build();

            // JPA Repository를 사용해 엔티티를 저장
            gradeRepository.save(bronze);
            gradeRepository.save(silver);
            gradeRepository.save(gold);
        }
    }
}