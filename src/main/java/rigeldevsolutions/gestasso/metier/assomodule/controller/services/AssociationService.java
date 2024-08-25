package rigeldevsolutions.gestasso.metier.assomodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.AssoRepo;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.UpdateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.events.AssociationCreatedEvent;
import rigeldevsolutions.gestasso.metier.assomodule.model.mappers.AssoMapper;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.services.ICotisationService;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.StringUtils;

import java.util.List;

@Service @RequiredArgsConstructor
public class AssociationService implements IAssociationService
{
    private final AssoRepo assoRepo;
    private final AssoMapper assoMapper;
    private final ApplicationEventPublisher eventPublisher;
    @Override @Transactional
    public Association createAssociation(CreateAssociationDTO dto, ActionIdentifier ai)
    {
        Association association = assoMapper.mapToAssociation(dto);

        association = assoRepo.save(association);
        BeanUtils.copyProperties(ai, association);
        eventPublisher.publishEvent(new AssociationCreatedEvent(this, association, dto.getCreateSectionDTOS(), ai));
        return association;
    }

    @Override @Transactional
    public Association updateAssociation(UpdateAssociationDTO dto, ActionIdentifier ai)
    {
        Association association = assoRepo.findById(dto.getAssoId()).orElseThrow(()->new AppException("Association introuvable"));
        BeanUtils.copyProperties(ai, association);
        association.setAssoName(dto.getAssoName());
        association.setSigle(dto.getSigle());
        association.setDroitAdhesion(dto.getDroitAdhesion());
        association.setSituationGeo(dto.getSituationGeo());
        return association;
    }

    @Override
    public Page<ReadAssociationDTO> searchAssociations(String key, Pageable pageable)
    {
        key = StringUtils.stripAccentsToUpperCase(key);
        return assoRepo.searchAssociations(key, pageable);
    }
}