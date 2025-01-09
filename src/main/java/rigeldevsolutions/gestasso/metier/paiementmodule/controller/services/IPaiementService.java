package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.PaiementCotisationDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO;

import java.net.UnknownHostException;
import java.util.List;

public interface IPaiementService {
    VersementDTO createVersementCotisation(PaiementCotisationDTO dto, ActionIdentifier ai);
    Page<PaiementCotisationDTO> findPaiementsByCotisation(Long cotisationId);


    PaiementCotisationDTO getPaiementCotisationDto(PaiementCotisationDTO dto);

    VersementDTO createVersementCotisation(PaiementCotisationDTO dto, List<MultipartFile> files, ActionIdentifier ai) throws UnknownHostException;
    String getPeriodesVersement(Long versementId);
    String generateRecuVersement(Long versementId) throws Exception;
}
