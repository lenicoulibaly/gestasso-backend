package rigeldevsolutions.gestasso.authmodule.model.dtos.appuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.authmodule.model.dtos.validators.ExistingCodePays;
import rigeldevsolutions.gestasso.authmodule.model.dtos.validators.ValidCodeCivilite;
import rigeldevsolutions.gestasso.grademodule.model.dtos.ExistingGradeCode;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingAssoId;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingSectionId;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateAdherantDTO
{
    @NotNull(message = "Veuillez saisir le nom")
    @NotBlank(message = "Veuillez saisir le nom")
    private String firstName;
    @NotNull(message = "Veuillez saisir le prénom")
    @NotBlank(message = "Veuillez saisir le prénom")
    private String lastName;
    @Email(message = "Adresse mail invalide")
    private String email;
    @NotNull(message = "Veuillez saisir le numéro de téléphone")
    @NotBlank(message = "Veuillez saisir le numéro de téléphone")
    private String tel;
    private String lieuNaissance;
    @Past(message = "La date de naissance ne peut être future")
    private LocalDate dateNaissance;
    @ValidCodeCivilite
    private String codeCivilite;
    @ExistingCodePays
    private String codePays;
    private String maticuleFonctionnaire;
    @ExistingGradeCode
    private String gradeCode;
    private int indiceFonctionnaire;
    private String nomPere;
    private String nomMere;
    @ExistingSectionId
    private Long sectionId;
    @ExistingAssoId
    private Long assoId;
}