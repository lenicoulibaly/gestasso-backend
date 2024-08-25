package rigeldevsolutions.gestasso.metier.assomodule.controller.resources;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IActionIdentifierService;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.assomodule.controller.services.IAdhesionService;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateAdhesionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadMemberDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;
import rigeldevsolutions.gestasso.sharedmodule.constants.Requests;

@RestController @RequestMapping(path = "/adhesions")
@RequiredArgsConstructor
public class AdhesionController
{
    private final IAdhesionService adhesionService;
    private final IActionIdentifierService ais;
    @PostMapping(path = "/create")
    Adhesion createAdhesion(@Valid @RequestBody CreateAdhesionDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext("Création d'un utilisateur");
        Adhesion adhesion = adhesionService.createAdhesion(dto, ai);
        return adhesion;
    }

    @PutMapping(path = "/desister/{adhesionId}")
    void desister(@PathVariable Long adhesionId)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext("Résiliation d'un abonnement");
        adhesionService.seDesabonner(adhesionId, ai);
    }

    @GetMapping(path = "/search-members")
    Page<ReadMemberDTO> searchMembers(@RequestParam(defaultValue = "", required = false) String key,
                                      @RequestParam(required = false) Long assoId,
                                      @RequestParam(required = false) Long sectionId,
                                      @RequestParam(defaultValue = "0", required = false) int page,
                                      @RequestParam(defaultValue = Requests.PAGE_SIZE, required = false) int size)
    {
        return adhesionService.searchMembers(key, assoId, sectionId, PageRequest.of(page, size));
    }
}
