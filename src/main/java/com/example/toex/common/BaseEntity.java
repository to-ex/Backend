package com.example.toex.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class BaseEntity {
    @CreatedDate
    @Column(name = "created_dt", nullable = false, updatable = false)
    protected LocalDateTime createdDt;

    @LastModifiedDate
    @Column(name = "updated_dt")
    protected LocalDateTime updatedDt;

    @Column(name = "deleted_dt")
    protected LocalDateTime deletedDt;

    @Column(name = "del_yn", columnDefinition = "VARCHAR(1) default 'N'")
    public String delYn;

    @PrePersist
    public void prePersist(){
        this.delYn = "N";
    }
    public void delete(){
        this.delYn = "Y";
    }
}
