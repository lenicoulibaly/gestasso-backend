package rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators.ExistingCotisationId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VersementDTO
{
    private Long versementId;
    private String codeVersement;
    private LocalDate dateVersement;
    private BigDecimal montant;
    private String montantLettre;
    private boolean active;
    private String modePaiementCode;
    private String modePaiement;
    private String typePaiementCode;
    private String typePaiement;
    private Long adhesionId;
    private String firstName;
    private String lastName;
    private String email;
    @ExistingCotisationId
    private Long cotisationId;
    private String nomCotisation;
    private String motif;
    List<PaiementDTO> paiements;
}
