package rigeldevsolutions.gestasso.metier.prelevementmodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.authmodule.model.entities.HistoDetails;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeance;

import java.math.BigDecimal;

import static rigeldevsolutions.gestasso.sharedmodule.constants.PRECISION.QUARANTE_INT;
import static rigeldevsolutions.gestasso.sharedmodule.constants.PRECISION.VINGT_INT;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
@Audited @EntityListeners(AuditingEntityListener.class)
public class Prelevement extends HistoDetails
{
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRELEV_ID_GEN")
  @SequenceGenerator(name = "PRELEV_ID_GEN", sequenceName = "PRELEV_ID_GEN")
  private Long prelevementId;
  private Long nbrAdherant;
  @Column(precision = QUARANTE_INT, scale = VINGT_INT)
  private BigDecimal montant;
  private String montantLettre;
  private boolean active;

  @ManyToOne @JoinColumn(name = "echeance_id")
  private Echeance echeance;
  @ManyToOne @JoinColumn(name = "cotisation_id")
  private Cotisation cotisation;

  public Prelevement(Long prelevementId)
  {
    this.prelevementId = prelevementId;
  }
}