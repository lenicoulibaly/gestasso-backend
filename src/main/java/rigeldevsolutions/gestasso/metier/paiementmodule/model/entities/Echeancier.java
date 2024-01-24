package rigeldevsolutions.gestasso.metier.paiementmodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited @EntityListeners(AuditingEntityListener.class)
@Entity
public class Echeancier
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ECHEANCIER_ID_GEN")
    @SequenceGenerator(name = "ECHEANCIER_ID_GEN", sequenceName = "ECHEANCIER_ID_GEN", allocationSize = 10)
    Long echeancierId;
    @ManyToOne @JoinColumn(name = "TYPE_ID")
    private Type typeEcheancier;
    @Transient
    List<Echeance> echeances;
}
