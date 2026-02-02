package App.TaskHub.entity.BaseDomain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDomain<T extends Serializable> implements Serializable{

@Id
@GeneratedValue private UUID id;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    protected UUID createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    protected Instant createdDate;

    @LastModifiedBy
    @Column(name = "updated_by")
    protected UUID updatedBy;

    @LastModifiedDate
    @Column(name = "updated_date")
    protected Instant updatedDate;
// created_by и updated_by ИД ползователя кто создал или изменил (отделнном методе)
}
