package rigeldevsolutions.gestasso.metier.paiementmodule.controller.resources;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IActionIdentifierService;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.services.IEcheancierService;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.constants.EcheancierActions;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheancierDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheancierDTO;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/echeanciers")
public class EcheancierController
{
    private final IEcheancierService echeancierService;
    private final IActionIdentifierService ais;

    @GetMapping(path = "/{echeancierId}")
    public ReadEcheancierDTO getEcheancier(@PathVariable Long echeancierId)
    {
        return echeancierService.getEcheancier(echeancierId);
    }

    @GetMapping(path = "/naturels")
    public ReadEcheancierDTO getEcheancier(@RequestParam String frequenceTypeCode, @RequestParam int exeCode)
    {
        return echeancierService.getEcheancierNaturel(frequenceTypeCode, exeCode);
    }

    @PostMapping(path = "/create")
    public ReadEcheancierDTO createEcheancier(@RequestBody @Valid CreateEcheancierDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(EcheancierActions.CREATE_ECHEANCIER);
        return echeancierService.createEcheancier(dto, ai);
    }
}
