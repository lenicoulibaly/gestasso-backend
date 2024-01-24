package rigeldevsolutions.gestasso.authmodule.model.entities;

import rigeldevsolutions.gestasso.typemodule.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class AppPrivilege
{
    @Id
    private String privilegeCode;
    @Column(unique = true, columnDefinition = "text")
    private String privilegeName;
    @ManyToOne @JoinColumn
    private Type prvType;

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

    public AppPrivilege(String privilegeCode)
    {
        this.privilegeCode = privilegeCode;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o==null) return false;
        if(!(o instanceof AppPrivilege)) return false;
        AppPrivilege that = (AppPrivilege) o;
        return privilegeCode.equals(that.privilegeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(privilegeCode);
    }
}
