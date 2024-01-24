package rigeldevsolutions.gestasso.authmodule.model.dtos.asignation;

import rigeldevsolutions.gestasso.authmodule.model.dtos.approle.ExistingRoleCode;
import rigeldevsolutions.gestasso.authmodule.model.dtos.approle.UniqueRoleCode;
import rigeldevsolutions.gestasso.authmodule.model.dtos.approle.UniqueRoleName;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@UniqueRoleName
public class PrvsToRoleDTO
{
    private String roleName;
    @ExistingRoleCode
    private String roleCode;
    private Set<String> prvCodes = new HashSet<>();
    private LocalDate startsAt;
    private LocalDate endsAt;
    private boolean permanent;
}
