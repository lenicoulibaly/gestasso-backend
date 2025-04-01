package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.response.ReadDocDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.PaiementCotisationDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.List;

public interface IPaiementService {
    VersementDTO createVersementCotisation(PaiementCotisationDTO dto);
    Page<PaiementCotisationDTO> findPaiementsByCotisation(Long cotisationId);


    PaiementCotisationDTO getPaiementCotisationDto(PaiementCotisationDTO dto);

    VersementDTO createVersementCotisation(PaiementCotisationDTO dto, List<MultipartFile> files) throws UnknownHostException;
    String getPeriodesVersement(Long versementId);
    String generateRecuVersement(Long versementId) throws Exception;

    Page<VersementDTO> searchVersement(Long cotisationId, LocalDate from, LocalDate to, String key, Pageable of);

    List<PaiementCotisationDTO> getPaiementsByVersementId(Long versementId);

    List<ReadDocDTO> getDocsByVersementId(Long versementId);
}
