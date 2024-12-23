package rigeldevsolutions.gestasso.metier.paiementmodule.controller.resources;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IActionIdentifierService;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.services.IPaiementService;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.constants.PaiementActions;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.PaiementDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/paiements")
public class PaiementController
{
    private final IPaiementService paiementService;
    private final IActionIdentifierService ais;

    @PostMapping(path = "/create-versement")
    public VersementDTO createVersement(@RequestBody @Valid PaiementDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(PaiementActions.CREATE_VERSEMENT);
        return paiementService.createVersementCotisation(dto, ai);
    }
}
