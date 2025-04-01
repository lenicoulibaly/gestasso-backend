package rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingAdhesionId;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators.ExistingCotisationId;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.ExistingEcheanceId;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.validators.ExistingPrelevementId;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class DefautPrelevementDTO
{
  private Long defautPrelevementId;
  @NotNull(message = "Le montant du prélèvement ne peut être nul")
  @Positive(message = "Le montant du prélèvement doit être supérieur à 0")
  private BigDecimal montant;
  private String montantLettre;
  private String motifDefaut;
  private boolean active;

  @ExistingPrelevementId
  private Long prelevementId;

  @ExistingAdhesionId
  private Long adhesionId;
  private String userId;
  private String firstName;
  private String lastName;
  private String matriculeFonctionnaire;
  private String email;
  private String tel;

  @ExistingEcheanceId
  private Long echeanceId;
  private String nomEcheance;
  @ExistingCotisationId
  @NotNull(message = "L'ID de la cotisation ne peut être null'")
  private Long cotisationId;
  private String nomCotisation;
  private String motifCotifCotisation;

  public DefautPrelevementDTO(Long defautPrelevementId, BigDecimal montant, String montantLettre, String motifDefaut, boolean active, Long prelevementId,
                              Long adhesionId, String userId, Long echeanceId, String nomEcheance, Long cotisationId, String nomCotisation, String motifCotifCotisation)
  {
    this.defautPrelevementId = defautPrelevementId;
    this.montant = montant;
    this.montantLettre = montantLettre;
    this.motifDefaut = motifDefaut;
    this.active = active;
    this.prelevementId = prelevementId;
    this.adhesionId = adhesionId;
    this.userId = userId;
    this.echeanceId = echeanceId;
    this.nomEcheance = nomEcheance;
    this.cotisationId = cotisationId;
    this.nomCotisation = nomCotisation;
    this.motifCotifCotisation = motifCotifCotisation;
  }
}