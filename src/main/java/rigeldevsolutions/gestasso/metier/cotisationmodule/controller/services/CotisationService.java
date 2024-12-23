package rigeldevsolutions.gestasso.metier.cotisationmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.CreateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.ReadCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.UpdateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.mappers.CotisationMapper;
import rigeldevsolutions.gestasso.sharedmodule.constants.PRECISION;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;

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
        BeanUtils.copyProperties(ai, cotisation);
        return cotisationMapper.mapToReadCotisationDTO(cotisation);
    }

    @Override
    public Page<ReadCotisationDTO> searchCotisations(String key, Long assoId, Long sectionId, boolean actuel, Pageable pageable) {
        Page<ReadCotisationDTO> cotisationDTOS = cotisationRepo.searchCotisations(key, assoId, sectionId, actuel, pageable);
        return cotisationDTOS;
    }

    @Override
    public long determineNbrEcheances(Long cotisationId)
    {
        Cotisation cotisation = cotisationRepo.findById(cotisationId).orElseThrow(()->new AppException("Cotisation introuvable"));
        if(cotisation.getDateFinCotisation() == null) return PRECISION.INFINIT_NBR_ECHEANCES;
        LocalDate dateDebut = cotisation.getDateDebutCotisation();
        LocalDate dateFin = cotisation.getDateFinCotisation();
        if(dateDebut == null) dateDebut = cotisation.getCreatedAt().toLocalDate();
        return switch (cotisation.getFrequenceCotisation().getUniqueCode())
                {
                    case "JOURNALIERE" -> ChronoUnit.DAYS.between(dateDebut, dateFin);
                    case "HEBDOMADAIRE" -> ChronoUnit.WEEKS.between(dateDebut, dateFin);
                    case "MENSUEL" -> ChronoUnit.MONTHS.between(dateDebut, dateFin);
                    case "TRIMESTRIEL" -> ChronoUnit.MONTHS.between(dateDebut, dateFin)/3;
                    case "SEMESTRIEL" -> ChronoUnit.MONTHS.between(dateDebut, dateFin)/6;
                    default -> ChronoUnit.YEARS.between(dateDebut, dateFin);
                };
    }

    @Override
    public BigDecimal calculateMontantAttenduParMembre(Long cotisationId)
    {
        Cotisation cotisation = cotisationRepo.findById(cotisationId).orElseThrow(()->new AppException("Cotisation introuvable"));
        if(cotisation.getDateFinCotisation() == null) return PRECISION.INFINIT_MONTANT;
        BigDecimal montantCotisation = Optional.ofNullable(cotisation.getMontantCotisation()).orElse(ZERO);
        Long nbrEcheances = this.determineNbrEcheances(cotisationId);
        return montantCotisation.multiply(new BigDecimal(nbrEcheances));
    }
}
