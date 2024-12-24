package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.PaiementRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.VersementRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.PaiementCotisationDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeance;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Paiement;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Versement;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.mappers.PaiementMapper;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.MontantConverter;
import rigeldevsolutions.gestasso.sharedmodule.utilities.MontantFormater;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;
import static rigeldevsolutions.gestasso.sharedmodule.constants.PRECISION.CINQ;

@Service @RequiredArgsConstructor
public class PaiementService implements IPaiementService
{
    private final PaiementRepo paiementRepo;
    private final PaiementMapper paiementMapper;
    private final CotisationRepo cotisationRepo;
    private final VersementRepo versementRepo;
    private final IEcheanceService echeanceService;
    private final ICalculPaiementService calculPaiementService;

    @Override @Transactional
    public VersementDTO createVersementCotisation(PaiementCotisationDTO dto, ActionIdentifier ai)
    {
        BigDecimal resteAPayer = calculPaiementService.calculateResteAPayer(dto.getCotisationId(), dto.getAdhesionId());
        if(dto.getMontant().subtract(resteAPayer).compareTo(CINQ)>0) throw new AppException("Le montant du versement ne peut exéder " + MontantFormater.format(resteAPayer));
        Versement versement = paiementMapper.mapToVersement(dto);
        versement.setMontantLettre(MontantConverter.numberToLetter(dto.getMontant()));
        versement.setActive(true);
        BeanUtils.copyProperties(ai, versement);
        versement = versementRepo.save(versement);
        String codeVersement = "VERS-COT-" + versement.getVersementId();
        versement.setCodeVersement(codeVersement);
        BigDecimal montantCotisation = cotisationRepo.getMontantCotisation(dto.getCotisationId()).orElseThrow(()->new AppException("Le montant de la cotisation ne peut être nul"));;
        BigDecimal montantPaiement = dto.getMontant();
        int nbrPaiements = montantPaiement.divideToIntegralValue(montantCotisation).intValue();

        BigDecimal montantDernierPaiement = montantPaiement.remainder(montantCotisation);


        final Versement finalVersement = versement;
        List<ReadEcheanceDTO> echeances = echeanceService.getNextEcheancesToPay(nbrPaiements, dto.getCotisationId(), dto.getAdhesionId());

        List<PaiementCotisationDTO> paiements = echeances.stream().map(e->
                this.createPaiementCotisation(dto, e.getMontantEcheance(), e.getEcheanceId(), finalVersement, ai)
        ).collect(Collectors.toList());

        if(montantDernierPaiement.compareTo(ZERO)!=0)
        {
            ReadEcheanceDTO lastEcheance = echeanceService.getNextEcheance(echeances.get(echeances.size()-1).getEcheanceId());
            PaiementCotisationDTO lastPaiement = this.createPaiementCotisation(dto, montantDernierPaiement, lastEcheance.getEcheanceId(), finalVersement, ai);
            paiements.add(lastPaiement);
        }

        VersementDTO versementDTO = paiementMapper.mapToVersementDto(versement);
        versementDTO.setPaiements(paiements);
        return versementDTO;
    }

    private PaiementCotisationDTO createPaiementCotisation(PaiementCotisationDTO dto, BigDecimal montantPaiement, Long echeanceId, Versement versement, ActionIdentifier ai) {
        PaiementCotisationDTO paiementDTO = new PaiementCotisationDTO();
        BeanUtils.copyProperties(dto, paiementDTO);
        paiementDTO.setMontant(montantPaiement);

        Paiement paiement = paiementMapper.mapToPaiementCotisation(paiementDTO);
        paiement.setMontantLettre(MontantConverter.numberToLetter(montantPaiement));
        paiement.setActive(true);
        paiement.setEcheance(new Echeance(echeanceId));
        paiement.setVersement(versement);
        BeanUtils.copyProperties(ai, paiement);
        paiement = paiementRepo.save(paiement);
        String reference = "PAIE-COT-" + paiement.getPaiementId();
        paiement.setReference(reference);
        paiement = paiementRepo.save(paiement);
        paiementDTO = paiementMapper.mapToPaiementDTO(paiement);
        return paiementDTO;
    }


    @Override
    public Page<PaiementCotisationDTO> findPaiementsByCotisation(Long cotisationId)
    {
        return null;
    }

    @Override
    public PaiementCotisationDTO getPaiementCotisationDto(PaiementCotisationDTO dto)
    {
        Long cotisationId = dto.getCotisationId();
        Long adhesionId = dto.getAdhesionId();
        BigDecimal montantVersement = dto.getMontant();
        if(cotisationId == null) throw new AppException("Veuillez fournir l'ID de la cotisation");
        if(adhesionId == null) throw new AppException("Veuillez fournir l'ID de l'adhésion");
        if(montantVersement == null) montantVersement = ZERO;

        Cotisation cotisation = cotisationRepo.findById(cotisationId).orElseThrow(()->new AppException("Cotisation introuvable " + cotisationId));
        ReadEcheanceDTO echeance = echeanceService.getCurrentEcheanceToPay(cotisationId, adhesionId);
        BigDecimal montantRetard = calculPaiementService.calculateMontantRetard(cotisationId, adhesionId);
        BigDecimal montantVersementSouhaite = calculPaiementService.calculateMontantVersementSouhaite(cotisationId, adhesionId);
        Long nbrEcheancesSoldees = calculPaiementService.calculateNbrEcheancesSoldeesParVersement(cotisationId,adhesionId, montantVersement );
        ReadEcheanceDTO nextEcheanceAfterPaying = echeanceService.getNextEcheanceToPayAfterPaying(cotisationId, adhesionId, montantVersement);
        dto.setEcheanceCoursPaiement(echeance.getNomEcheance());
        dto.setRetardPaiement(montantRetard);
        dto.setMontantVersementSouhaite(montantVersementSouhaite);
        dto.setNbrEcheancesSoldeesParCeVersement(nbrEcheancesSoldees);
        dto.setProchaineEcheance(nextEcheanceAfterPaying == null ? "Cotisation soldée" : nextEcheanceAfterPaying.getNomEcheance());
        dto.setMontantProchaineEcheance(nextEcheanceAfterPaying == null ? ZERO : nextEcheanceAfterPaying.getMontantEcheance());
        dto.setNomCotisation(cotisation.getNomCotisation());
        dto.setMotif(cotisation.getMotif());
        return dto;
    }
}