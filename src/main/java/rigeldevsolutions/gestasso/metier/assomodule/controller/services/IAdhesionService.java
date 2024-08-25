package rigeldevsolutions.gestasso.metier.assomodule.controller.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateAdhesionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadMemberDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;

public interface IAdhesionService
{
    Adhesion createAdhesion(CreateAdhesionDTO dto, ActionIdentifier ai);
    void seDesabonner(Long adhesionId, ActionIdentifier ai);
    Page<ReadMemberDTO> searchMembers(String key, Long assoId, Long sectionId, Pageable pageable);
}
