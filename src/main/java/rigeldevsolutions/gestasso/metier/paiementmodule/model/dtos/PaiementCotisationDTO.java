package rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingAdhesionId;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators.ExistingCotisationId;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.UniqueReference;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.ValidModePaiement;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.ValidTypePaiement;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@UniqueReference
public class PaiementCotisationDTO
{
    private Long PaiementId;
    private String reference;
    private LocalDate datePaiement;
    @NotNull(message = "Le montant du paiement ne peut être nul")
    @Positive(message = "Le montant du paiement doit être supérieur à 0")
    private BigDecimal montant;
    private String montantLettre;
    private boolean active;
    @ValidModePaiement
    @NotNull(message = "Le mode de paiement ne peut être nul")
    @NotBlank(message = "Le mode de paiement ne peut être nul")
    private String modePaiementCode;
    private String modePaiement;
    @ValidTypePaiement
    @NotNull(message = "Le type de paiement ne peut être nul")
    @NotBlank(message = "Le type de paiement ne peut être nul")
    private String typePaiementCode;
    private String typePaiement;
    @ExistingAdhesionId
    private Long adhesionId;
    private String firstName;
    private String lastName;
    private String email;
    @ExistingCotisationId
    private Long cotisationId;
    private String nomCotisation;
    private String motif;
    private Long versementId;
    private String codeVersement;

    private String echeanceCoursPaiement;
    private BigDecimal retardPaiement;
    private Long nbrEcheancesSoldeesParCeVersement;
    private String prochaineEcheance;
    private BigDecimal montantProchaineEcheance;
    private BigDecimal montantVersementSouhaite;
}