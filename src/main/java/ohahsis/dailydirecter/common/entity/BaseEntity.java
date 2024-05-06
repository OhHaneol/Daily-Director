package ohahsis.dailydirecter.common.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass   // 부모 클래스(BaseEntity)는 테이블과 매핑하지 않고, 자신을 상속 받는 자식 클래스에게 자신의 칼럼만 매핑정보로 제공(즉 테이블 두 개의 매핑이 아니라, 자식 테이블에 컬럼 추가)
@EntityListeners(AuditingEntityListener.class)  // 해당 클래스에 Auditing 기능을 포함하도록 함. TODO Jpa config 파일 등록?
public abstract class BaseEntity {

    @Column(name = "created_at")
    @CreatedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @LastModifiedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedAt;

}
