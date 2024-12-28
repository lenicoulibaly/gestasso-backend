package rigeldevsolutions.gestasso.metier.paiementmodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.authmodule.model.entities.HistoDetails;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;

import java.math.BigDecimal;
import java.time.LocalDate;

import static rigeldevsolutions.gestasso.sharedmodule.constants.PRECISION.QUARANTE_INT;
import static rigeldevsolutions.gestasso.sharedmodule.constants.PRECISION.VINGT_INT;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity @Audited
@EntityListeners(AuditingEntityListener.class)
public class Versement extends HistoDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VERS_ID_GEN")
    @SequenceGenerator(name = "VERS_ID_GEN", sequenceName = "VERS_ID_GEN")
    private Long versementId;
    private String codeVersement;
    private LocalDate dateVersement;
    @Column(precision = QUARANTE_INT, scale = VINGT_INT)
    private BigDecimal montant;
    private String montantLettre;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "mode_paiement_unique_code")
    private Type modePaiement;
    @ManyToOne @JoinColumn(name = "type_paiement_unicode_code")
    private Type typePaiement;//Paiement cotisation, paiement acquisition terrain, paiement acquisition logement, paiement prêt scolaire
    @ManyToOne @JoinColumn(name = "adhesion_id")
    private Adhesion adhesion;
    @ManyToOne @JoinColumn(name = "cotisation_id")
    private Cotisation cotisation;

    public Versement(Long versementId)
    {
        this.versementId = versementId;
    }
}
