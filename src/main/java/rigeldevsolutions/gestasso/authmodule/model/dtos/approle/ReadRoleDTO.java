package rigeldevsolutions.gestasso.authmodule.model.dtos.approle;

import rigeldevsolutions.gestasso.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import rigeldevsolutions.gestasso.sharedmodule.dtos.SelectOption;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadRoleDTO
{
    private String roleCode;
    private String roleName;
    private Set<ReadPrvDTO> privileges;
    private List<SelectOption> privilegeOptions;
}
