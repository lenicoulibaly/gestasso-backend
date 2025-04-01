package rigeldevsolutions.gestasso.metier.assomodule.controller.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.metier.assomodule.controller.services.IAssociationService;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.UpdateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.sharedmodule.constants.Requests;

import java.net.UnknownHostException;
import java.util.Base64;

@RestController @RequiredArgsConstructor @RequestMapping("/associations")
public class AssociationController
{
    private final IAssociationService associationService;
    private final ObjectMapper objectMapper;

    //@PostMapping(path = "/create")
    Association createAssociation(@Valid @RequestBody CreateAssociationDTO dto)
    {
        return associationService.createAssociation(dto);
    }

    @PostMapping(path = "/create")
    Association createAssociation(@Valid @RequestPart("data") String dtoJsonString, @RequestPart("logo")MultipartFile logo) throws JsonProcessingException, UnknownHostException
    {
        CreateAssociationDTO dto = objectMapper.readValue(dtoJsonString, CreateAssociationDTO.class);
        return associationService.createAssociation(dto, logo);
    }

    @PutMapping(path = "/update")
    Association updateAssociation(@Valid @RequestBody UpdateAssociationDTO dto)
    {
        return associationService.updateAssociation(dto);
    }

    @GetMapping(path = "/search")
    public Page<ReadAssociationDTO> searchAssociations(@RequestParam(defaultValue = "", required = false) String key,
                                                @RequestParam(required = false) Long strId,
                                                @RequestParam(defaultValue = "0", required = false) int page,
                                                @RequestParam(defaultValue = Requests.PAGE_SIZE, required = false) int size)
    {
        return associationService.searchAssociations(key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/find-by-id/{assoId}")
    public ReadAssociationDTO findById(@PathVariable Long assoId)
    {
        return associationService.findById(assoId);
    }

    @GetMapping(path = "/generate-fiche-adhesion/{assoId}")
    public String generateFicheAdhesion(@PathVariable Long assoId) throws Exception {
        byte[] bytes = associationService.generateFicheAdhesion(assoId);
        String base64String =  Base64.getEncoder().encodeToString(bytes);
        return base64String;
    }
}
