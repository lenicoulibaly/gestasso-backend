package rigeldevsolutions.gestasso.metier.prelevementmodule.controller.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.metier.prelevementmodule.controller.service.IPrelevementService;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.DefautPrelevementDTO;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.PrelevementDTO;
import rigeldevsolutions.gestasso.sharedmodule.utilities.StringUtils;

import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/prelevement-cotisation")
public class PrelevementController
{
    private final IPrelevementService prelevementService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "/save")
    public PrelevementDTO save(@RequestPart("data") String jsonString, @RequestPart(name = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {
        PrelevementDTO dto = objectMapper.readValue(jsonString, PrelevementDTO.class);
        return prelevementService.savePrelevement(dto, files);
    }

    @PostMapping(path = "/save-defaut-prelevement")
    public DefautPrelevementDTO saveDefautPrelevement(@RequestBody @Valid DefautPrelevementDTO dto)
    {
        return prelevementService.saveDefautPrelevementCotisation(dto);
    }

    @GetMapping(path = "/search")
    Page<PrelevementDTO> search(@PathVariable Long cotisationId,
                                @RequestParam(defaultValue = "", required = false) String key,
                                @RequestParam(defaultValue = "0", required = false) int page,
                                @RequestParam(defaultValue = "10", required = false) int size)
    {
        key = StringUtils.stripAccentsToUpperCase(key);
        return prelevementService.searchPrelevementsCotisation(cotisationId, key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/defaut-prelevement-page")
    Page<DefautPrelevementDTO> getDefautPrelevementList(@PathVariable Long prelevementId,
                                                        @RequestParam(defaultValue = "0", required = false) int page,
                                                        @RequestParam(defaultValue = "10", required = false) int size)
    {
        return prelevementService.getDefautPrelevementCotisationPage(prelevementId, PageRequest.of(page, size));
    }

    @PostMapping(path = "/edit-dto")
    PrelevementDTO getPrelevementEditDto(@RequestBody PrelevementDTO dto)
    {
        return prelevementService.getPrelevementEditDto(dto);
    }
}
