package rigeldevsolutions.gestasso.metier.paiementmodule.controller.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.response.ReadDocDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.services.IPaiementService;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.constants.PaiementActions;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.PaiementCotisationDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/paiements")
public class PaiementController
{
    private final IPaiementService paiementService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "/create-versement-cotisation")
    public VersementDTO createVersement(@RequestBody @Valid PaiementCotisationDTO dto)
    {
        return paiementService.createVersementCotisation(dto);
    }

    @PostMapping(path = "/get-paiement-cotisation-dto")
    public PaiementCotisationDTO getPaiementCotisationDTO(@RequestBody @Valid PaiementCotisationDTO dto)
    {
        return paiementService.getPaiementCotisationDto(dto);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public VersementDTO createPaiement(@RequestPart("data") String dtoJson, @RequestPart("files") List<MultipartFile> files) throws JsonProcessingException, UnknownHostException
    {
        PaiementCotisationDTO dto = objectMapper.readValue(dtoJson, PaiementCotisationDTO.class);

        VersementDTO versement = paiementService.createVersementCotisation(dto, files);
        return versement;
    }

    @GetMapping(path = "/generate-recu-paiement/{versementId}")
    public String generateRecuPaiement(@PathVariable Long versementId) throws Exception
    {
        String base64String = paiementService.generateRecuVersement(versementId);
        return base64String;
    }

    @GetMapping(path = "/versements/{cotisationId}")
    public Page<VersementDTO> searchVersements(@PathVariable Long cotisationId,
                                               @RequestParam(required = false) LocalDate from,
                                               @RequestParam(required = false) LocalDate to,
                                               @RequestParam(required = false, defaultValue = "") String key,
                                               @RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "10") int size)
    {
        return paiementService.searchVersement(cotisationId, from, to, key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/by-versement/{versementId}")
    public List<PaiementCotisationDTO> getPaiementsByVersementId(@PathVariable Long versementId)
    {
        return paiementService.getPaiementsByVersementId(versementId);
    }

    @GetMapping(path = "/docs-by-versement/{versementId}")
    public List<ReadDocDTO> getDocsByVersement(@PathVariable Long versementId)
    {
        return paiementService.getDocsByVersementId(versementId);
    }
}
