package rigeldevsolutions.gestasso.metier.paiementmodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited @EntityListeners(AuditingEntityListener.class)
@Entity
public class Echeance
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ECHEANCE_ID_GEN")
    @SequenceGenerator(name = "ECHEANCE_ID_GEN", sequenceName = "ECHEANCE_ID_GEN", allocationSize = 10)
    private Long echeanceId;
    private LocalDate dateEcheance;
    private LocalDate dateButtoire;
    private BigDecimal montantEcheance;
    private BigDecimal tauxEcheance;
    @ManyToOne @JoinColumn(name = "ECHEANCIER_ID")
    private Echeancier echeancier;

    public Echeance(Long echeanceId) {
        this.echeanceId = echeanceId;
    }
}
