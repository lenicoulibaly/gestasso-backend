package rigeldevsolutions.gestasso.metier.assomodule.controller.resources;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import rigeldevsolutions.gestasso.metier.assomodule.controller.services.IAdhesionService;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.AdhesionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;
import rigeldevsolutions.gestasso.sharedmodule.constants.Requests;
import rigeldevsolutions.gestasso.sharedmodule.dtos.SelectOption;

import java.util.List;

@RestController @RequestMapping(path = "/adhesions")
@RequiredArgsConstructor
public class AdhesionController
{
    private final IAdhesionService adhesionService;
    @PostMapping(path = "/create")
    Adhesion createAdhesion(@Valid @RequestBody AdhesionDTO dto)
    {
        Adhesion adhesion = adhesionService.createUserAndAdhesion(dto);
        return adhesion;
    }

    @PutMapping(path = "/update")
    Adhesion updateAdhesion(@Valid @RequestBody AdhesionDTO dto)
    {
        Adhesion adhesion = adhesionService.updateMembre(dto);
        return adhesion;
    }

    @PutMapping(path = "/desabonner/{adhesionId}")
    void desister(@PathVariable Long adhesionId)
    {
        adhesionService.seDesabonner(adhesionId);
    }

    @GetMapping(path = "/search-members")
    Page<AdhesionDTO> searchMembers(@RequestParam(defaultValue = "", required = false) String key,
                                      @RequestParam(required = false) Long assoId,
                                      @RequestParam(required = false) Long sectionId,
                                      @RequestParam(defaultValue = "0", required = false) int page,
                                      @RequestParam(defaultValue = Requests.PAGE_SIZE, required = false) int size)
    {
        return adhesionService.searchAdhsions(key, assoId, sectionId, PageRequest.of(page, size));
    }

    @GetMapping(path = "/all-options")
    List<SelectOption> getAdhesionOptions(@RequestParam Long assoId)
    {
        return adhesionService.getOptions(assoId);
    }

    @GetMapping(path = "/get-membre-dto")
    AdhesionDTO getMembreDto(@RequestParam(required = false) String identifier)
    {
        return adhesionService.getMembreDTO(identifier);
    }
}