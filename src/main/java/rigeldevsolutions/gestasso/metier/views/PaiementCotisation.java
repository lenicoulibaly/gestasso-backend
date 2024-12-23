package rigeldevsolutions.gestasso.metier.views;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "v_paiement_cotisation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PaiementCotisation
{
    @Id
    private Long rownum;
    private Long cotisationId;
    private String nomCotisation;
    private BigDecimal montantCotisation;
    private Long echeancierId;
    private String echeancier;
    private Long echeanceId;
    private LocalDate dateEcheance;
    private String nomEcheance;
    private Boolean echeanceEchu;
    private Boolean retardEcheance;
    private BigDecimal paiementEcheance;
    private BigDecimal resteEcheance;
    private Boolean echeanceSolde;
    private String firstName;
    private String lastName;
    private String email;
    private Long adhesionId;
    private Long versementId;
    private String codeVersement;
}