package rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import rigeldevsolutions.gestasso.authmodule.model.dtos.asignation.CoherentDates;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingAssoId;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingSectionId;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators.AssoIdAndSectionIdNotNullTogether;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators.UniqueCotisationName;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@UniqueCotisationName
@AssoIdAndSectionIdNotNullTogether
@CoherentDates  @NotNull(message = "Aucune donnée parvenue")
public class CreateCotisationDTO
{
    @NotNull(message = "Veuillez nommer la cotisation")
    @NotBlank(message = "Veuillez nommer la cotisation")
    private String nomCotisation;
    @NotNull(message = "Le montant de la cotisation ne peut être nul")
    @NotBlank(message = "Le montant de la cotisation ne peut être nul")
    private BigDecimal montantCotisation;
    @NotNull(message = "La fréquence de cotisation ne peut être nulle")
    @NotBlank(message = "La fréquence de cotisation ne peut être nulle")
    private String frequenceCotisation;
    @NotNull(message = "Le mode de prélèvement ne peut être nul")
    @NotBlank(message = "Le mode de prélèvement ne peut être nul")
    private String modePrelevement;
    @NotNull(message = "La date de début de la cotisation est obligatoire")
    private LocalDate dateDebutCotisation;
    private LocalDate dateFinCotisation;
    private int delaiDeRigueurEnJours;
    @ExistingAssoId
    private Long assoId;
    @ExistingSectionId
    private Long sectionId;
}
