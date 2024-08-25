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
public class AppFunction extends HistoDetails
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FNC_ID_GEN")
    @SequenceGenerator(name = "FNC_ID_GEN", sequenceName = "FNC_ID_GEN", allocationSize = 10)
    protected Long id;

    private String name;
    @ManyToOne @JoinColumn(name = "USER_ID")
    private AppUser user;
    @ManyToOne @JoinColumn(name = "TYPE_CODE")
    private Type type;
    @ManyToOne @JoinColumn(name = "SECTION_ID")
    private Section section;
    @ManyToOne @JoinColumn(name = "ASSOCIATION_ID")
    private Association association;
    protected int fncStatus;// 1 == actif, 2 == inactif, 3 == revoke
    protected LocalDate startsAt;
    protected LocalDate endsAt;

    public AppFunction(String name, AppUser user, int fncStatus, LocalDate startsAt, LocalDate endsAt) {
        this.name = name;
        this.user = user;
        this.fncStatus = fncStatus;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public AppFunction(Long fncId) {
        this.id = fncId;
    }
    @Override
    public String toString() {
        return id + "_" + name;
    }
}
