package rigeldevsolutions.gestasso.metier.assomodule.controller.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.AdhesionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;
import rigeldevsolutions.gestasso.sharedmodule.dtos.SelectOption;

import java.util.List;
import java.util.Optional;

public interface IAdhesionService
{
    Adhesion createUserAndAdhesion(AdhesionDTO dto);

    @Transactional
    Adhesion updateMembre(AdhesionDTO dto);

    void seDesabonner(Long adhesionId);
    Page<AdhesionDTO> searchAdhsions(String key, Long assoId, Long sectionId, Pageable pageable);
    AdhesionDTO getMembreDTO(String uniqueIdentifier);

    List<SelectOption> getOptions(Long assoId);

    Optional<Adhesion> findByEmailAndSection(String email, Long sectionId);

    Optional<Adhesion> findByEmailAndAsso(String email, Long assoId);
}
