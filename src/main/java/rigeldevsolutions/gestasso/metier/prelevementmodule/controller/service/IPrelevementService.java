package rigeldevsolutions.gestasso.metier.prelevementmodule.controller.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.DefautPrelevementDTO;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.PrelevementDTO;

import java.util.List;

@Validated
public interface IPrelevementService
{
    PrelevementDTO savePrelevement(@Valid PrelevementDTO dto, List<MultipartFile> files, ActionIdentifier ai);
    Page<PrelevementDTO> searchPrelevementsCotisation(Long cotisationId, String key, Pageable pageable);
    Page<DefautPrelevementDTO> getDefautPrelevementCotisationPage(Long prelevementId, Pageable pageable);

    DefautPrelevementDTO saveDefautPrelevementCotisation(DefautPrelevementDTO dto, ActionIdentifier ai);
    PrelevementDTO getPrelevementEditDto(PrelevementDTO dto);
}
