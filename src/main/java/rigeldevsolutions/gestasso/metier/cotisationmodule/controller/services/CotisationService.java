package rigeldevsolutions.gestasso.metier.cotisationmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.UpdateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.CreateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.ReadCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.UpdateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.mappers.CotisationMapper;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;

@Service @RequiredArgsConstructor
public class CotisationService implements ICotisationService
{
    private final CotisationRepo cotisationRepo;
    private final CotisationMapper cotisationMapper;
    @Override @Transactional
    public ReadCotisationDTO createCotisation(CreateCotisationDTO dto, ActionIdentifier ai)
    {
        Cotisation cotisation = cotisationMapper.mapToCotisation(dto);
        BeanUtils.copyProperties(ai, cotisation);
        cotisation = cotisationRepo.save(cotisation);
        return cotisationMapper.mapToReadCotisationDTO(cotisation);
    }

    @Override @Transactional
    public ReadCotisationDTO updateCotisation(UpdateCotisationDTO dto, ActionIdentifier ai)
    {
        Cotisation cotisation = cotisationRepo.findById(dto.getCotisationId()).orElseThrow(()->new AppException("Cotisation introuvable"));
        Type frequence = cotisation.getFrequenceCotisation();
        Type modePrelevement = cotisation.getModePrelevement();
        Type newFrequence = frequence == null || frequence.getUniqueCode() == null || !frequence.getUniqueCode().equals(dto.getFrequenceCotisation()) ? new Type(dto.getFrequenceCotisation()) : frequence;
        Type newModePrelevement = modePrelevement == null || modePrelevement.getUniqueCode() == null || !modePrelevement.getUniqueCode().equals(dto.getModePrelevement()) ? new Type(dto.getModePrelevement()) : frequence;

        cotisation.setNomCotisation(dto.getNomCotisation());
        cotisation.setMontantCotisation(dto.getMontantCotisation());
        cotisation.setDelaiDeRigueurEnJours(dto.getDelaiDeRigueurEnJours());

        cotisation.setFrequenceCotisation(newFrequence);
        cotisation.setModePrelevement(newModePrelevement);
        BeanUtils.copyProperties(ai, cotisation);
        return cotisationMapper.mapToReadCotisationDTO(cotisation);
    }

    @Override
    public Page<ReadCotisationDTO> searchCotisations(String key, Long assoId, Long sectionId, boolean actuel, Pageable pageable) {
        Page<ReadCotisationDTO> cotisationDTOS = cotisationRepo.searchCotisations(key, assoId, sectionId, actuel, pageable);
        return cotisationDTOS;
    }
}
