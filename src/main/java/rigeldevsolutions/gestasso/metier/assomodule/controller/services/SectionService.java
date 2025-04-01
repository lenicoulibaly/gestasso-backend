package rigeldevsolutions.gestasso.metier.assomodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.SectionRepo;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.UpdateSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section;
import rigeldevsolutions.gestasso.metier.assomodule.model.mappers.SectionMapper;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.structuremodule.model.entities.Structure;

import java.util.List;
import java.util.Objects;

@Service @RequiredArgsConstructor
public class SectionService implements ISectionService
{
    private final SectionRepo sectionRepo;
    private final SectionMapper sectionMapper;
    @Override @Transactional
    public ReadSectionDTO createSection(CreateSectionDTO dto)
    {
        Section section = sectionMapper.mapToSection(dto);
        section = sectionRepo.save(section);
        return sectionMapper.mapToReadSectionDTO(section);
    }

    @Override @Transactional
    public ReadSectionDTO updateSection(UpdateSectionDTO dto)
    {
        if(dto == null) throw new AppException("Le corps de la requête est null");
        Section section = sectionRepo.findById(dto.getSectionId()).orElseThrow(()->new AppException("Section introuvable"));
        Long dtoStrId = dto.getStrId();
        Long sectionStrId = section.getStrTutelle() == null ? null : section.getStrTutelle().getStrId();

        if(!Objects.equals(dtoStrId, sectionStrId))
            section.setStrTutelle(dtoStrId == null ? null : new Structure(dtoStrId));

        return sectionMapper.mapToReadSectionDTO(section);
    }

    @Override
    public Page<ReadSectionDTO> searchSections(String key, Long assoId, Long strId, Pageable pageable) {
        return sectionRepo.searchSections(key, assoId, strId, pageable);
    }

    @Override
    public Section createSectionDeBase(Association association) {
        Section sectionMere = new Section();
        sectionMere.setAssociation(association);
        sectionMere.setSectionName("Section de base");
        sectionMere.setSituationGeo(association.getSituationGeo());
        sectionMere = sectionRepo.save(sectionMere);
        return sectionMere;
    }

    @Override
    public List<ReadSectionDTO> getAssociationSections(Long assoId)
    {
        return sectionRepo.findbyAssoId(assoId);
    }
}
