package rigeldevsolutions.gestasso.metier.assomodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;
import rigeldevsolutions.gestasso.authmodule.model.entities.HistoDetails;

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
    private String userId;
    @Transient
    private KeycloakUser keycloakUser;

    public Adhesion(Long adhesionId)
    {
        this.adhesionId = adhesionId;
    }
}
