package rigeldevsolutions.gestasso.metier.assomodule.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingAssoId;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.UniqueSectionName;
import rigeldevsolutions.gestasso.structuremodule.model.dtos.ExistingOrNullStrId;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@UniqueSectionName
public class CreateSectionDTO
{
    @NotNull(message = "Le nom de la section est obligatoire")
    @NotBlank(message = "Le nom de la section est obligatoire")
    private String sectionName;
    private String sigle;
    @ExistingAssoId @NotNull(message = "Veuillez selectionner l'association")
    private Long assoId;
    @ExistingOrNullStrId
    private Long strId;
    private String situationGeo;
}