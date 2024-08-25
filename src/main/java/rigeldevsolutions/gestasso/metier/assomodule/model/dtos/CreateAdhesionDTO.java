package rigeldevsolutions.gestasso.metier.assomodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appuser.ExistingUserId;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingAssoId;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingSectionId;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateAdhesionDTO
{
    @ExistingUserId
    private Long userId;
    @ExistingSectionId
    private Long sectionId;
    @ExistingAssoId
    private Long assoId;
}
