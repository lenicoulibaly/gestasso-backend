package rigeldevsolutions.gestasso.authmodule.model.entities;

import lombok.*;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @DiscriminatorValue("PRV_TO_ROLE")
@Audited @EntityListeners(AuditingEntityListener.class)
public class PrvToRoleAss extends Assignation
{
    @ManyToOne @JoinColumn(name = "PRV_ID")
    private AppPrivilege privilege;
    @ManyToOne @JoinColumn(name = "ROLE_ID")
    private AppRole role;

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

    public PrvToRoleAss(Long assId, int assStatus, LocalDate startsAt, LocalDate endsAt, AppPrivilege privilege, AppRole role) {
        super(assId, assStatus, startsAt, endsAt);
        this.privilege = privilege;
        this.role = role;
    }
}
