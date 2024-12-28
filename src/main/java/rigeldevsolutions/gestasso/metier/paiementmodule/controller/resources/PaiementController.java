package rigeldevsolutions.gestasso.metier.paiementmodule.controller.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.archivemodule.controller.service.IServiceDocument;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IActionIdentifierService;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.services.IPaiementService;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.constants.PaiementActions;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.PaiementCotisationDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/paiements")
public class PaiementController
{
    private final IPaiementService paiementService;
    private final IActionIdentifierService ais;
    private final ObjectMapper objectMapper;
    private final IServiceDocument docService;

    @PostMapping(path = "/create-versement-cotisation")
    public VersementDTO createVersement(@RequestBody @Valid PaiementCotisationDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(PaiementActions.CREATE_VERSEMENT);
        return paiementService.createVersementCotisation(dto, ai);
    }

    @PostMapping(path = "/get-paiement-cotisation-dto")
    public PaiementCotisationDTO getPaiementCotisationDTO(@RequestBody @Valid PaiementCotisationDTO dto)
    {
        return paiementService.getPaiementCotisationDto(dto);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public VersementDTO createPaiement(@RequestPart("data") String dtoJson, @RequestPart("files") List<MultipartFile> files) throws JsonProcessingException, UnknownHostException
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(PaiementActions.CREATE_VERSEMENT);

        PaiementCotisationDTO dto = objectMapper.readValue(dtoJson, PaiementCotisationDTO.class);

        VersementDTO versement = paiementService.createVersementCotisation(dto, files, ai);
        return versement;
    }

    @PostMapping(value = "/debug-create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> debugRequest(HttpServletRequest request) throws IOException, ServletException {
        Collection<Part> parts = request.getParts();
        for (Part part : parts) {
            System.out.println("Nom de la partie : " + part.getName());
            System.out.println("Type de contenu : " + part.getContentType());
            System.out.println("Taille de la partie : " + part.getSize());
        }
        return ResponseEntity.ok("Debug terminé");
    }
}
