package rigeldevsolutions.gestasso.authmodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @DiscriminatorValue("ROLE_TO_FNC")
@Audited @EntityListeners(AuditingEntityListener.class)
public class RoleToFncAss extends Assignation
{
    @ManyToOne @JoinColumn(name = "ROLE_ID")
    private AppRole role;
    @ManyToOne @JoinColumn(name = "FNC_ID")
    private AppFunction function;

    @CreatedDate
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
    @CreatedBy
    @Column(name = "CreatedBy", length = 50)
    private String createdBy;
    @LastModifiedDate
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
    @LastModifiedBy
    @Column(name = "UpdatedBy", length = 50)
    private String updatedBy;
    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;
    @Column(name = "DeletedBy", length = 50)
    private String deletedBy;
    @Column(name = "isDeleted", length = 50)
    private Boolean isDeleted = false;
    private String action;
    private String connectionId;

    public RoleToFncAss(Long assId, int assStatus, LocalDate startsAt, LocalDate endsAt, AppRole role, AppFunction function) {
        super(assId, assStatus, startsAt, endsAt);
        this.role = role;
        this.function = function;
    }
}
