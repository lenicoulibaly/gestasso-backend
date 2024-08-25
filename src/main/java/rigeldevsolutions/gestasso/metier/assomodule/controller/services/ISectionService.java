package rigeldevsolutions.gestasso.metier.assomodule.controller.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.UpdateSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section;

public interface ISectionService {
    ReadSectionDTO createSection(CreateSectionDTO dto, ActionIdentifier ai);

    ReadSectionDTO updateSection(UpdateSectionDTO dto, ActionIdentifier ai);

    Page<ReadSectionDTO> searchSections(String key, Long assoId, Long strId, Pageable pageable);

    Section createSectionDeBase(Association association, ActionIdentifier ai);
}
