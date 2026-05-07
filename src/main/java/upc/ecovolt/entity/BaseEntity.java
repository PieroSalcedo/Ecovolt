package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter @Setter
//@MappedSuperclass -> define una clase cuyas propiedades deben ser heredadas por las entidades que la extiendan
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(name = "status",nullable = false)
    private Integer status = 1;

    @CreatedDate
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 50)
    private String createdBy = "SYSTEM";

    @Column(name = "updated_by",length = 50)
    private String updatedBy;

}
