package rigeldevsolutions.gestasso.metier.assomodule.controller.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateMembreDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadMembreDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;

public interface IMembreService
{
    Adhesion createMembre(CreateMembreDTO dto, ActionIdentifier ai);

    @Transactional
    Adhesion updateMembre(ReadMembreDTO dto, ActionIdentifier ai);

    void seDesabonner(Long adhesionId, ActionIdentifier ai);
    Page<ReadMembreDTO> searchMembers(String key, Long assoId, Long sectionId, Pageable pageable);
    CreateMembreDTO getMembreDTO(String uniqueIdentifier);
}
