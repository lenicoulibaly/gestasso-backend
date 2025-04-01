package rigeldevsolutions.gestasso.metier.cotisationmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.CreateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.ReadCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.UpdateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.mappers.CotisationMapper;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.EcheanceRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;

import java.time.LocalDate;

@Service @RequiredArgsConstructor
public class CotisationService implements ICotisationService
{
    private final CotisationRepo cotisationRepo;
    private final CotisationMapper cotisationMapper;
    private final EcheanceRepo echeanceRepo;

    @Override
    public LocalDate getDateFin(Cotisation cotisation)
    {
        LocalDate dateFin = cotisation.getDateFinCotisation();
        if(dateFin == null)
        {
            String typeFrequence = cotisation.getFrequenceCotisation().getUniqueCode();
            ReadEcheanceDTO lastEcheance = echeanceRepo.getLastEcheance(typeFrequence);
            dateFin = lastEcheance.getDateEcheance();
        }
        return dateFin;
    }
    @Override @Transactional
    public ReadCotisationDTO createCotisation(CreateCotisationDTO dto)
    {
        Cotisation cotisation = cotisationMapper.mapToCotisation(dto);
        cotisation = cotisationRepo.save(cotisation);
        return cotisationMapper.mapToReadCotisationDTO(cotisation);
    }

    @Override @Transactional
    public ReadCotisationDTO updateCotisation(UpdateCotisationDTO dto)
    {
        Cotisation cotisation = cotisationRepo.findById(dto.getCotisationId()).orElseThrow(()->new AppException("Cotisation introuvable"));
        Type frequence = cotisation.getFrequenceCotisation();
        Type modePrelevement = cotisation.getModePrelevement();
        Type newFrequence = frequence == null || frequence.getUniqueCode() == null || !frequence.getUniqueCode().equals(dto.getFrequenceCotisationCode()) ? new Type(dto.getFrequenceCotisationCode()) : frequence;
        Type newModePrelevement = modePrelevement == null || modePrelevement.getUniqueCode() == null || !modePrelevement.getUniqueCode().equals(dto.getModePrelevementCode()) ? new Type(dto.getModePrelevementCode()) : frequence;

        cotisation.setNomCotisation(dto.getNomCotisation());
        cotisation.setMotif(dto.getMotif());
        cotisation.setMontantCotisation(dto.getMontantCotisation());
        cotisation.setDelaiDeRigueurEnJours(dto.getDelaiDeRigueurEnJours());

        cotisation.setFrequenceCotisation(newFrequence);
        cotisation.setModePrelevement(newModePrelevement);
        cotisation.setDateDebutCotisation(dto.getDateDebutCotisation());
        cotisation.setDateFinCotisation(dto.getDateFinCotisation());
        return cotisationMapper.mapToReadCotisationDTO(cotisation);
    }

    @Override
    public Page<ReadCotisationDTO> searchCotisations(String key, Long assoId, Long sectionId, boolean actuel, Pageable pageable) {
        Page<ReadCotisationDTO> cotisationDTOS = cotisationRepo.searchCotisations(key, assoId, sectionId, actuel, pageable);
        return cotisationDTOS;
    }
}
