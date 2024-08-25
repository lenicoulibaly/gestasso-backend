package rigeldevsolutions.gestasso.metier.assomodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.authmodule.model.entities.HistoDetails;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.enums.Frequence;
import rigeldevsolutions.gestasso.structuremodule.model.entities.Structure;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Audited @EntityListeners(AuditingEntityListener.class)
@Entity
public class Association extends HistoDetails
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ASSO_ID_GEN")
    @SequenceGenerator(name = "ASSO_ID_GEN", sequenceName = "ASSO_ID_GEN", allocationSize = 10)
    private Long assoId;
    private String assoName;
    private String situationGeo;
    private String sigle;
    private BigDecimal droitAdhesion;

    public Association(Long assoId) {
        this.assoId = assoId;
    }

    @Override
    public String toString() {
        return assoId + "_" + assoName + "_" + sigle;
    }
}
