package rigeldevsolutions.gestasso.metier.assomodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.structuremodule.model.dtos.ExistingStrId;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateSectionDTO
{
    private Long sectionId;
    private String sectionName;
    private String situationGeo;
    private String sigle;
    @ExistingStrId
    private Long strId;
}
