package rigeldevsolutions.gestasso.metier.assomodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.authmodule.model.events.AdherantCreatedEvent;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.AdhesionRepo;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadMemberDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.mappers.AdhesionMapper;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateAdhesionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.StringUtils;

@Service @RequiredArgsConstructor
public class AdhesionService implements IAdhesionService
{
    private final AdhesionMapper adhesionMapper;
    private final AdhesionRepo adhesionRepo;
    @Override @Transactional
    public Adhesion createAdhesion(CreateAdhesionDTO dto, ActionIdentifier ai)
    {
        Adhesion adhesion = adhesionMapper.mapToAdhesion(dto);
        BeanUtils.copyProperties(ai, adhesion);
        return adhesionRepo.save(adhesion);
    }

    @Override @Transactional
    public void seDesabonner(Long adhesionId, ActionIdentifier ai)
    {
        Adhesion adhesion = adhesionRepo.findById(adhesionId).orElseThrow(()-> new AppException("Adhésion introuvable"));
        adhesion.setActive(false);
        BeanUtils.copyProperties(ai, adhesion);
    }

    @Override
    public Page<ReadMemberDTO> searchMembers(String key, Long assoId, Long sectionId, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        return adhesionRepo.searchMembers(key, assoId, sectionId, pageable);
    }

    @Override @TransactionalEventListener
    public void onAdherantCreatedEvent(AdherantCreatedEvent event)
    {
        CreateAdhesionDTO dto = new CreateAdhesionDTO();
        dto.setAssoId(event.getDto().getAssoId());
        dto.setSectionId(event.getDto().getSectionId());
        dto.setUserId(event.getUser().getUserId());
        this.createAdhesion(dto, event.getAi());
    }
}