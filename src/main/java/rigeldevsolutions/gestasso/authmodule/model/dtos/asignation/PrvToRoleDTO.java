package rigeldevsolutions.gestasso.authmodule.model.dtos.asignation;

import rigeldevsolutions.gestasso.authmodule.model.dtos.appprivilege.ExistingPrivilegeCode;
import rigeldevsolutions.gestasso.authmodule.model.dtos.approle.ExistingRoleCode;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrvToRoleDTO
{
    @ExistingRoleCode
    private Long roleId;
    @ExistingPrivilegeCode
    private Long privilegeId;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private boolean permanent;
}
