package rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction;

import rigeldevsolutions.gestasso.authmodule.model.dtos.appuser.ExistingUserId;
import rigeldevsolutions.gestasso.typemodule.model.dtos.ExistingTypeCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateFncDTO
{
    private Long ecoleId;
    private String classeIds;
    private String name;
    @ExistingUserId
    private Long userId;
    @ExistingTypeCode
    private String typeCode;
    protected int fncStatus;// 1 == actif, 2 == inactif, 3 == revoke
    protected LocalDate startsAt;
    protected LocalDate endsAt;
    private Set<String> roleCodes;
}
