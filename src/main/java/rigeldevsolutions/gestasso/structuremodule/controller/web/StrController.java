package rigeldevsolutions.gestasso.structuremodule.controller.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.UserRepo;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IJwtService;
import rigeldevsolutions.gestasso.sharedmodule.dtos.SelectOption;
import rigeldevsolutions.gestasso.structuremodule.controller.repositories.StrRepo;
import rigeldevsolutions.gestasso.structuremodule.controller.service.IStrService;
import rigeldevsolutions.gestasso.structuremodule.model.dtos.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/structures")
public class StrController
{
    private final IStrService strService;
    private final StrRepo strRepo;
    private final StrMapper strMapper;
    private final UserRepo userRepo;
    private final IJwtService jwtService;


    @PostMapping(path = "/changeAnchor")
    public ReadStrDTO ChangeAnchor(@Valid ChangeAnchorDTO dto)
    {
        return strService.changeAncrage(dto);
    }

    @PostMapping(path = "/create")
    public ReadStrDTO createStr(@Valid CreateStrDTO dto)
    {
        return strService.createStr(dto);
    }

    @PostMapping(path = "/update")
    public ReadStrDTO updateStr(@Valid UpdateStrDTO dto)
    {
        return strService.updateStr(dto);
    }

    @GetMapping(path = "/get-by-child-type/{childTypeCode}") @ResponseBody
    public List<ReadStrDTO> getStrByChildType(@PathVariable String childTypeCode)
    {
        return strService.getStrByChildType(childTypeCode);
    }

    @GetMapping(path = "/loadStrTreeView/{strId}") @ResponseBody @PreAuthorize("permitAll()")
    public List<StrTreeView> loadStrTreeView(@PathVariable Long strId)
    {
        return this.strService.loadStrTreeView(strId);
    }

    @GetMapping(path = "/loadStrTreeView/{strId}/{critere}") @ResponseBody @PreAuthorize("permitAll()")
    public List<StrTreeView> loadStrTreeView(@PathVariable Long strId, @PathVariable String critere)
    {
        return this.strService.loadStrTreeView(strId, critere);
    }

    @GetMapping(path = "/countAllChildren/{strId}") @ResponseBody @PreAuthorize("permitAll()")
    public long countAllChildren(@PathVariable Long strId)
    {
        return this.strRepo.countAllChildren(strId);
    }

    @GetMapping(path = "/getChildrenMaxLevel/{strId}") @ResponseBody @PreAuthorize("permitAll()")
    public long getChildrenMaxLevel(@PathVariable long strId)
    {
        return this.strRepo.getChildrenMaxLevel(strRepo.getStrCode(strId));
    }

    @GetMapping(path = "/getAllChildren/{strId}") @ResponseBody @PreAuthorize("permitAll()")
    public List<ReadStrDTO> getChildren(@PathVariable long strId)
    {
        return this.strRepo.findAllChildren(strId).stream().map(strMapper::mapToReadSimpleReadStrDto).collect(Collectors.toList());
    }

    @GetMapping(path="/all-options")
    public List<SelectOption> getStrOptions()
    {
        return strService.getStrAllOptions();
    }
}
