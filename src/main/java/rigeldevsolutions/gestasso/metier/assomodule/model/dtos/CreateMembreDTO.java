package rigeldevsolutions.gestasso.metier.assomodule.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appuser.UniqueEmail;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appuser.UniqueTel;
import rigeldevsolutions.gestasso.authmodule.model.dtos.validators.ExistingCodePays;
import rigeldevsolutions.gestasso.authmodule.model.dtos.validators.ValidCodeCivilite;
import rigeldevsolutions.gestasso.grademodule.model.dtos.ExistingGradeCode;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingAssoId;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingSectionId;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @UniqueEmail @UniqueTel
public class CreateMembreDTO
{
    private Long userId;
    @ExistingSectionId
    private Long sectionId;
    @ExistingAssoId @NotNull(message = "Veuillez selectionner l'association")
    private Long assoId;

    private String matriculeFonctionnaire;
    private String nomCivilite;
    @ExistingCodePays
    private String codePays;
    @ExistingGradeCode
    private String gradeCode;
    private int indiceFonctionnaire;

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

    public CreateMembreDTO(Long userId, String matriculeFonctionnaire, String email, String tel, String firstName, String lastName, String lieuNaissance, LocalDate dateNaissance, String codeCivilite, String nomCivilite) {
        this.userId = userId;
        this.matriculeFonctionnaire = matriculeFonctionnaire;
        this.email = email;
        this.tel = tel;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lieuNaissance = lieuNaissance;
        this.dateNaissance = dateNaissance;
        this.codeCivilite = codeCivilite;
        this.nomCivilite = nomCivilite;
    }
}
