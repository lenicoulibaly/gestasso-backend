package rigeldevsolutions.gestasso.authmodule.controller.services.spec;

import rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction.CreateFncDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction.ReadFncDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction.UpdateFncDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appuser.AuthResponseDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.asignation.SetAuthoritiesToFunctionDTO;

import java.net.UnknownHostException;

public interface IFunctionService
{
    Long getActiveCurrentFunctionId(Long userId);
    String getActiveCurrentFunctionName(Long userId);
    ReadFncDTO createFnc(CreateFncDTO dto) throws UnknownHostException;
    AuthResponseDTO setFunctionAsDefault(Long fncId) throws UnknownHostException;
    void revokeFunction(Long fncId) throws UnknownHostException;
    void restoreFunction(Long fncId) throws UnknownHostException;
    //ReadFncDTO setFunctionAuthorities(SetAuthoritiesToFunctionDTO dto);
    ReadFncDTO updateFunction(UpdateFncDTO dto) throws UnknownHostException;

    ReadFncDTO getActiveCurrentFunction(Long userId);

    ReadFncDTO getFunctioninfos(Long foncId);
}
