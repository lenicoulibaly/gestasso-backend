package rigeldevsolutions.gestasso.metier.cotisationmodule.controller.resources;

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
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.services.ICotisationService;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.CreateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.ReadCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.UpdateCotisationDTO;
import rigeldevsolutions.gestasso.sharedmodule.constants.Requests;

@RestController @RequiredArgsConstructor @RequestMapping("/cotisations")
public class CotisationController
{
    private final ICotisationService cotisationService;
    private final IActionIdentifierService ais;

    @PostMapping(path = "/create")
    ReadCotisationDTO createCotisation(@Valid @RequestBody CreateCotisationDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext("Création d'une cotisation");
        return cotisationService.createCotisation(dto, ai);
    }

    @PutMapping(path = "/update")
    ReadCotisationDTO updateCotisation(@Valid @RequestBody UpdateCotisationDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext("Modification d'une cotisation");
        return cotisationService.updateCotisation(dto, ai);
    }

    @GetMapping(path = "/search")
    Page<ReadCotisationDTO> searchCotisations(@RequestParam(defaultValue = "", required = false) String key,

                                                @RequestParam(required = false) Long assoId,
                                                @RequestParam(required = false) Long sectionId,
                                                @RequestParam(defaultValue = "0", required = false) int page,
                                                @RequestParam(defaultValue = "true", required = false) boolean actuel,
                                                @RequestParam(defaultValue = Requests.PAGE_SIZE, required = false) int size)
    {
        return cotisationService.searchCotisations(key, assoId, sectionId, actuel, PageRequest.of(page, size));
    }
}
