package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.services.ICotisationService;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.PaiementRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.VersementRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.PaiementDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeance;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Paiement;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Versement;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.mappers.PaiementMapper;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.MontantConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
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
    private final ICotisationService cotisationService;
    private final DecimalFormat decimalFormat;

    @Override @Transactional
    public VersementDTO createVersementCotisation(PaiementDTO dto, ActionIdentifier ai)
    {
        BigDecimal resteAPayer = this.calculateResteAPayer(dto.getCotisationId(), dto.getAdhesionId());
        if(dto.getMontant().subtract(resteAPayer).compareTo(CINQ)>0) throw new AppException("Le montant du versement ne peut exéder " + decimalFormat.format(resteAPayer));
        Versement versement = paiementMapper.mapToVersement(dto);
        versement.setMontantLettre(MontantConverter.numberToLetter(dto.getMontant()));
        versement.setActive(true);
        BeanUtils.copyProperties(ai, versement);
        versement = versementRepo.save(versement);
        String codeVersement = "VERS-COT-" + versement.getVersementId();
        versement.setCodeVersement(codeVersement);
        BigDecimal montantCotisation = cotisationRepo.getMotantCotisation(dto.getCotisationId());
        BigDecimal montantPaiement = dto.getMontant();
        int nbrPaiements = montantPaiement.divideToIntegralValue(montantCotisation).intValue();

        BigDecimal montantDernierPaiement = montantPaiement.remainder(montantCotisation);


        final Versement finalVersement = versement;
        List<ReadEcheanceDTO> echeances = echeanceService.getNextEcheancesToPay(nbrPaiements, dto.getCotisationId(), dto.getAdhesionId());

        List<PaiementDTO> paiements = echeances.stream().map(e->
                this.createPaiementCotisation(dto, e.getMontantEcheance(), e.getEcheanceId(), finalVersement, ai)
        ).collect(Collectors.toList());

        if(montantDernierPaiement.compareTo(ZERO)!=0)
        {
            ReadEcheanceDTO lastEcheance = echeanceService.getNextEcheance(echeances.get(echeances.size()-1).getEcheanceId());
            PaiementDTO lastPaiement = this.createPaiementCotisation(dto, montantDernierPaiement, lastEcheance.getEcheanceId(), finalVersement, ai);
            paiements.add(lastPaiement);
        }

        VersementDTO versementDTO = paiementMapper.mapToVersementDto(versement);
        versementDTO.setPaiements(paiements);
        return versementDTO;
    }

    private PaiementDTO createPaiementCotisation(PaiementDTO dto, BigDecimal montantPaiement, Long echeanceId, Versement versement, ActionIdentifier ai) {
        PaiementDTO paiementDTO = new PaiementDTO();
        BeanUtils.copyProperties(dto, paiementDTO);
        paiementDTO.setMontant(montantPaiement);

        Paiement paiement = paiementMapper.mapToPaiementCotisation(paiementDTO);
        paiement.setMontantLettre(MontantConverter.numberToLetter(montantPaiement));
        paiement.setActive(true);
        paiement.setEcheance(new Echeance(echeanceId));
        paiement.setVersement(versement);
        BeanUtils.copyProperties(ai, paiement);
        paiement = paiementRepo.save(paiement);
        String reference = "PAIE-COT" + paiement.getPaiementId();
        paiement.setReference(reference);
        paiement = paiementRepo.save(paiement);
        paiementDTO = paiementMapper.mapToPaiementDTO(paiement);
        return paiementDTO;
    }


    @Override
    public Page<PaiementDTO> findPaiementByCotisation(Long cotisationId)
    {
        return null;
    }

    @Override
    public BigDecimal calculateResteAPayer(Long cotisationId, Long adhesionId)
    {
        BigDecimal dejaPaye = Optional.ofNullable(paiementRepo.calculateDejaPaye(cotisationId, adhesionId)).orElse(ZERO);
        BigDecimal montantAttenduParMembre = cotisationService.calculateMontantAttenduParMembre(cotisationId);
        return montantAttenduParMembre.subtract(dejaPaye);
    }
}