package rigeldevsolutions.gestasso.metier.assomodule.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.dtos.ProfileDto;
import rigeldevsolutions.gestasso.authmodule.model.dtos.validators.ExistingCodePays;
import rigeldevsolutions.gestasso.authmodule.model.dtos.validators.ValidCodeCivilite;
import rigeldevsolutions.gestasso.grademodule.model.dtos.ExistingGradeCode;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AdhesionDTO
{
    private String userId;
    @ExistingSectionId
    private Long sectionId;
    @ExistingAssoId @NotNull(message = "Veuillez selectionner l'association")
    private Long assoId;
    @ExistingAdhesionId
    private Long adhesionId;
    @UniqueMatricule
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
    @UniqueEmail @NotNull(message = "Le mail est obligatoire")
    private String email;
    @NotNull(message = "Veuillez saisir le numéro de téléphone")
    @NotBlank(message = "Veuillez saisir le numéro de téléphone")
    @UniqueTel
    private String tel;
    private String lieuNaissance;
    @Past(message = "La date de naissance ne peut être future")
    private LocalDate dateNaissance;
    @ValidCodeCivilite @NotNull(message = "La civilité est obligatoire")
    private String codeCivilite;
    private String sectionName;
    private String assoName;
    private boolean enabled;
    private List<ProfileDto> profileDtos;

    public AdhesionDTO(String userId, Long sectionId, Long assoId, Long adhesionId, String sectionName, String assoName) {
        this.userId = userId;
        this.sectionId = sectionId;
        this.assoId = assoId;
        this.adhesionId = adhesionId;
        this.sectionName = sectionName;
        this.assoName = assoName;
    }

    public AdhesionDTO(String userId, String matriculeFonctionnaire, String email, String tel, String firstName, String lastName, String lieuNaissance, LocalDate dateNaissance, String codeCivilite, String nomCivilite) {
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

    public AdhesionDTO(String userId, Long sectionId, Long assoId, Long adhesionId, String matriculeFonctionnaire, String nomCivilite, String codePays, String gradeCode, int indiceFonctionnaire, String firstName, String lastName, String email, String tel, String lieuNaissance, LocalDate dateNaissance, String codeCivilite, String sectionName, String assoName) {
        this.userId = userId;
        this.sectionId = sectionId;
        this.assoId = assoId;
        this.adhesionId = adhesionId;
        this.matriculeFonctionnaire = matriculeFonctionnaire;
        this.nomCivilite = nomCivilite;
        this.codePays = codePays;
        this.gradeCode = gradeCode;
        this.indiceFonctionnaire = indiceFonctionnaire;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tel = tel;
        this.lieuNaissance = lieuNaissance;
        this.dateNaissance = dateNaissance;
        this.codeCivilite = codeCivilite;
        this.sectionName = sectionName;
        this.assoName = assoName;
    }
}
