package rigeldevsolutions.gestasso.authmodule.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@MappedSuperclass
@Audited @EntityListeners(AuditingEntityListener.class)
public class HistoDetails
{
    @Column(name = "action_name")
    protected String actionName;
    @Column(name = "action_id")
    protected String actionId;
    @Column(name = "connexion_id")
    protected String connexionId;
    @CreatedDate
    @Column(name = "created_at")
    protected LocalDateTime createdAt;
    @CreatedBy
    @Column(name = "created_by", length = 50)
    protected String createdBy;
    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    protected String updatedBy;
    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;
    @Column(name = "deleted_by", length = 50)
    protected String deletedBy;
    @Column(name = "is_deleted", length = 50)
    protected Boolean isDeleted = false;
}
