package rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.response.ReadDocDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators.ExistingCotisationId;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.ExistingEcheanceId;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class PrelevementDTO
{
  private Long prelevementId;
  @NotNull(message = "Le nombre d'adhérant ne peut être nul")
  @Positive(message = "Le nombre d'adhérant doit être supérieur à 0")
  private Long nbrAdherant;
  @NotNull(message = "Le montant du prélèvement ne peut être nul")
  @Positive(message = "Le montant du prélèvement doit être supérieur à 0")
  private BigDecimal montant;
  private String montantLettre;
  private boolean active;
  @ExistingEcheanceId
  @NotNull(message = "L'ID de l'écheance ne peut être nul")
  private Long echeanceId;
  private String nomEcheance;
  @ExistingCotisationId
  @NotNull(message = "L'ID de la cotisation ne peut être nul")
  private Long cotisationId;
  private String nomCotisation;
  private String motif;
  private List<ReadDocDTO> docs;

  private List<DefautPrelevementDTO> defautPrelevements;

  public PrelevementDTO(Long prelevementId, Long nbrAdherant, BigDecimal montant, String montantLettre, boolean active, Long echeanceId, String nomEcheance, Long cotisationId, String nomCotisation, String motif) {
    this.prelevementId = prelevementId;
    this.nbrAdherant = nbrAdherant;
    this.montant = montant;
    this.montantLettre = montantLettre;
    this.active = active;
    this.echeanceId = echeanceId;
    this.nomEcheance = nomEcheance;
    this.cotisationId = cotisationId;
    this.nomCotisation = nomCotisation;
    this.motif = motif;
  }

  public PrelevementDTO(Long cotisationId, String nomCotisation, String motif)
  {
    this.cotisationId = cotisationId;
    this.nomCotisation = nomCotisation;
    this.motif = motif;
  }
}