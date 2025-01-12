package rigeldevsolutions.gestasso.metier.prelevementmodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.authmodule.model.entities.HistoDetails;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;

import java.math.BigDecimal;

import static rigeldevsolutions.gestasso.sharedmodule.constants.PRECISION.QUARANTE_INT;
import static rigeldevsolutions.gestasso.sharedmodule.constants.PRECISION.VINGT_INT;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class DefautPrelevement extends HistoDetails
{
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEFAUT_PRELEV_ID_GEN")
  @SequenceGenerator(name = "DEFAUT_PRELEV_ID_GEN", sequenceName = "DEFAUT_PRELEV_ID_GEN")
  private Long defautPrelevementId;
  @ManyToOne @JoinColumn(name = "adhesion_id")
  private Adhesion adhesion;
  @Column(precision = QUARANTE_INT, scale = VINGT_INT)
  private BigDecimal montant;
  private String montantLettre;
  private String motif;
  private boolean active;

  @ManyToOne @JoinColumn(name = "prelevement_id")
  private Prelevement prelevement;
}