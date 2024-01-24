package rigeldevsolutions.gestasso.authmodule.controller.services.spec;

import rigeldevsolutions.gestasso.authmodule.model.dtos.approle.CreateRoleDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.approle.ReadRoleDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.asignation.PrvsToRoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.sharedmodule.dtos.SelectOption;

import java.net.UnknownHostException;
import java.util.List;

public interface IRoleService
{
    ReadRoleDTO createRole(CreateRoleDTO dto) throws UnknownHostException;
    Page<ReadRoleDTO> searchRoles(String searchKey, Pageable pageable);

    @Transactional
    ReadRoleDTO updateRole(PrvsToRoleDTO dto) throws UnknownHostException;

    List<SelectOption> getRoleOptions(List<String> roleCodes);
}
