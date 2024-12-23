package rigeldevsolutions.gestasso.metier.assomodule.controller.resources;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IActionIdentifierService;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.assomodule.controller.services.ISectionService;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.UpdateSectionDTO;
import rigeldevsolutions.gestasso.sharedmodule.constants.Requests;

import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/sections")
public class SectionController
{
    private final ISectionService sectionService;
    private final IActionIdentifierService ais;

    @PostMapping(path = "/create")
    ReadSectionDTO createSection(@Valid @RequestBody CreateSectionDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext("Création d'une section");
        return sectionService.createSection(dto, ai);
    }

    @PutMapping(path = "/update")
    ReadSectionDTO updateSection(@Valid @RequestBody UpdateSectionDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext("Modification d'une section");
        return sectionService.updateSection(dto, ai);
    }

    @GetMapping(path = "/search")
    Page<ReadSectionDTO> searchSections(@RequestParam(defaultValue = "", required = false) String key,
                                        @RequestParam(required = false) Long assoId,
                                        @RequestParam(required = false) Long strId,
                                        @RequestParam(defaultValue = "0", required = false) int page,
                                        @RequestParam(defaultValue = Requests.PAGE_SIZE, required = false) int size)
    {
        return sectionService.searchSections(key, assoId, strId, PageRequest.of(page, size));
    }

    @GetMapping(path = "/find-by-asso/{assoId}")
    List<ReadSectionDTO> getAssociationSections(@PathVariable Long assoId)
    {
        return sectionService.getAssociationSections(assoId);
    }
}
