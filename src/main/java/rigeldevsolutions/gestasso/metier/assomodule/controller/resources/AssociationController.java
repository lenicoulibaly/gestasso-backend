package rigeldevsolutions.gestasso.metier.assomodule.controller.resources;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IActionIdentifierService;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.assomodule.controller.services.IAssociationService;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.UpdateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.sharedmodule.constants.Requests;

@RestController @RequiredArgsConstructor @RequestMapping("/associations")
public class AssociationController
{
    private final IAssociationService associationService;
    private final IActionIdentifierService ais;

    @PostMapping(path = "/create")
    Association createAssociation(@Valid @RequestBody CreateAssociationDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext("Création d'une association et ses sections");
        return associationService.createAssociation(dto, ai);
    }

    @PutMapping(path = "/update")
    Association updateAssociation(@Valid @RequestBody UpdateAssociationDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext("Modification d'une association");
        return associationService.updateAssociation(dto, ai);
    }

    @GetMapping(path = "/search")
    Page<ReadAssociationDTO> searchAssociations(@RequestParam(defaultValue = "", required = false) String key,
                                                @RequestParam(required = false) Long strId,
                                                @RequestParam(defaultValue = "0", required = false) int page,
                                                @RequestParam(defaultValue = Requests.PAGE_SIZE, required = false) int size)
    {
        return associationService.searchAssociations(key, PageRequest.of(page, size));
    }
}
