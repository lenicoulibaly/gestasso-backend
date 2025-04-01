package rigeldevsolutions.gestasso.metier.paiementmodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;
import rigeldevsolutions.gestasso.authmodule.model.entities.HistoDetails;

import java.math.BigDecimal;

import static rigeldevsolutions.gestasso.sharedmodule.constants.PRECISION.QUARANTE_INT;
import static rigeldevsolutions.gestasso.sharedmodule.constants.PRECISION.VINGT_INT;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
@Audited @EntityListeners(AuditingEntityListener.class)
public class Paiement extends HistoDetails
{
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAIE_ID_GEN")
  @SequenceGenerator(name = "PAIE_ID_GEN", sequenceName = "PAIE_ID_GEN")
  private Long paiementId;
  private String reference;
  @Column(precision = QUARANTE_INT, scale = VINGT_INT)
  private BigDecimal montant;
  private String montantLettre;
  private boolean active;

  @ManyToOne @JoinColumn(name = "echeance_id")
  private Echeance echeance;
  @ManyToOne @JoinColumn(name = "versement_id")
  private Versement versement;
  private String userId;
  @Transient
  private KeycloakUser keycloakUser;
}