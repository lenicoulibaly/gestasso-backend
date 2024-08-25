package rigeldevsolutions.gestasso.metier.assomodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.authmodule.model.entities.HistoDetails;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor //@Builder
@Audited @EntityListeners(AuditingEntityListener.class)
@Entity
public class Adhesion extends HistoDetails
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADHESION_ID_GEN")
    @SequenceGenerator(name = "ADHESION_ID_GEN", sequenceName = "ADHESION_ID_GEN", allocationSize = 10)
    private Long adhesionId;
    @ManyToOne @JoinColumn(name = "ASSO_ID")
    private Association association;
    @ManyToOne @JoinColumn(name = "SECTION_ID")
    private Section section;
    private boolean active;
    @ManyToOne @JoinColumn(name = "MEMBER_ID")
    private AppUser member;
}
