package rigeldevsolutions.gestasso.metier.assomodule.model.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appuser.ExistingUserId;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingAdhesionId;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingSectionId;
import rigeldevsolutions.gestasso.typemodule.model.dtos.ExistingTypeCode;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @NotNull(message = "Aucune donnée fournie")
public class ReadMembreDTO
{
    @ExistingAdhesionId
    private Long adhesionId;
    @ExistingUserId
    private Long userId;
    private String firstName;
    private String lastName;
    private String matriculeFonctionnaire;
    private String email;
    private String tel;
    private String lieuNaissance;
    private LocalDate dateNaissance;
    @ExistingTypeCode(message = "Civilité inconnue")
    private String codeCivilite;
    private String civilite;
    private String paysCode;
    private String nationalite;
    private String nomPere;
    private String nomMere;
    private boolean active;
    private boolean notBlocked;
    @ExistingSectionId
    private Long sectionId;
    private String sectionName;
    private Long assoId;
    private String assoName;
}
