package rigeldevsolutions.gestasso.metier.cotisationmodule.controller.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.CreateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.ReadCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.UpdateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation;

import java.time.LocalDate;

public interface ICotisationService
{
    LocalDate getDateFin(Cotisation cotisation);

    ReadCotisationDTO createCotisation(CreateCotisationDTO dto);

    ReadCotisationDTO updateCotisation(UpdateCotisationDTO dto);

    Page<ReadCotisationDTO> searchCotisations(String key, Long assoId, Long sectionId, boolean actuel, Pageable pageable);
}
