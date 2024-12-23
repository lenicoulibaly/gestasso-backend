package rigeldevsolutions.gestasso.metier.assomodule.controller.resources;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IActionIdentifierService;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.assomodule.controller.services.IMembreService;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateMembreDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadMembreDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;
import rigeldevsolutions.gestasso.sharedmodule.constants.Requests;

@RestController @RequestMapping(path = "/adhesions")
@RequiredArgsConstructor
public class AdhesionController
{
    private final IMembreService adhesionService;
    private final IActionIdentifierService ais;
    @PostMapping(path = "/create")
    Adhesion createAdhesion(@Valid @RequestBody CreateMembreDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext("Création d'un membre d'association");
        Adhesion adhesion = adhesionService.createMembre(dto, ai);
        return adhesion;
    }

    @PutMapping(path = "/update")
    Adhesion updateAdhesion(@Valid @RequestBody ReadMembreDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext("Modification des informations d'un membre de section");
        Adhesion adhesion = adhesionService.updateMembre(dto, ai);
        return adhesion;
    }

    @PutMapping(path = "/desabonner/{adhesionId}")
    void desister(@PathVariable Long adhesionId)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext("Résiliation d'un abonnement");
        adhesionService.seDesabonner(adhesionId, ai);
    }

    @GetMapping(path = "/search-members")
    Page<ReadMembreDTO> searchMembers(@RequestParam(defaultValue = "", required = false) String key,
                                      @RequestParam(required = false) Long assoId,
                                      @RequestParam(required = false) Long sectionId,
                                      @RequestParam(defaultValue = "0", required = false) int page,
                                      @RequestParam(defaultValue = Requests.PAGE_SIZE, required = false) int size)
    {
        return adhesionService.searchMembers(key, assoId, sectionId, PageRequest.of(page, size));
    }

    @GetMapping(path = "/get-membre-dto")
    CreateMembreDTO getMembreDto(@RequestParam(required = false) String identifier)
    {
        return adhesionService.getMembreDTO(identifier);
    }
}