package rigeldevsolutions.gestasso.authmodule.model.entities;

import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;
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
@Entity
@Audited @EntityListeners(AuditingEntityListener.class)
public class AppFunction
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FNC_ID_GEN")
    @SequenceGenerator(name = "FNC_ID_GEN", sequenceName = "FNC_ID_GEN", allocationSize = 10)
    protected Long id;

    private String name;
    @ManyToOne @JoinColumn(name = "USER_ID")
    private AppUser user;
    @ManyToOne @JoinColumn(name = "TYPE_ID")
    private Type type;
    @ManyToOne @JoinColumn(name = "SECTION_ID")
    private Section section;
    @ManyToOne @JoinColumn(name = "ASSOCIATION_ID")
    private Association association;
    protected int fncStatus;// 1 == actif, 2 == inactif, 3 == revoke
    protected LocalDate startsAt;
    protected LocalDate endsAt;

    public AppFunction(Long id, String name, AppUser user, Type type, Section section, Association association, int fncStatus, LocalDate startsAt, LocalDate endsAt) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.type = type;
        this.section = section;
        this.association = association;
        this.fncStatus = fncStatus;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

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

    public AppFunction(Long fncId) {
        this.id = fncId;
    }
}
