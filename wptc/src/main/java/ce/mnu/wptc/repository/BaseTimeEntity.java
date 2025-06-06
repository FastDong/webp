package ce.mnu.wptc.repository;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@MappedSuperclass // 다른 엔티티 클래스들이 이 클래스를 상속할 경우, 필드(컬럼)들도 상속받도록 설정
@EntityListeners(AuditingEntityListener.class) // 시간에 대한 Auditing 기능 포함
public abstract class BaseTimeEntity {

 @CreatedDate // 엔티티가 생성되어 저장될 때 시간이 자동 저장
 private LocalDateTime createdAt;

 @LastModifiedDate // 조회한 엔티티의 값을 변경할 때 시간이 자동 저장
 private LocalDateTime updatedAt;

 // getter
 public LocalDateTime getCreatedAt() {
     return createdAt;
 }

 public LocalDateTime getUpdatedAt() {
     return updatedAt;
 }
}