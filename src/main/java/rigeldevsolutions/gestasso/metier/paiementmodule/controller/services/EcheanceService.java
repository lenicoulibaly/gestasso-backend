package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.EcheanceRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.UpdateEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeance;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeancier;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.mappers.EcheanceMapper;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;

@Service @RequiredArgsConstructor
public class EcheanceService implements IEcheanceService
{
    private final EcheanceMapper echeanceMapper;
    private final EcheanceRepo echeanceRepo;

    @Override @Transactional
    public ReadEcheanceDTO createEcheance(CreateEcheanceDTO dto, ActionIdentifier ai)
    {
        Echeance echeance = echeanceMapper.mapToEcheance(dto);
        echeance = echeanceRepo.save(echeance);
        BeanUtils.copyProperties(ai, echeance);
        return echeanceMapper.mapToReadEcheanceDTO(echeance);
    }

    @Override
    public ReadEcheanceDTO updateEcheance(UpdateEcheanceDTO dto, ActionIdentifier ai)
    {
        Echeance echeance = echeanceRepo.findById(dto.getEcheanceId()).orElseThrow(()->new AppException("Echéance introuvable"));
        echeance.setEcheancier(new Echeancier(dto.getEcheancierId()));
        BeanUtils.copyProperties(dto, echeance, "echeancier");
        BeanUtils.copyProperties(ai, echeance);
        return echeanceMapper.mapToReadEcheanceDTO(echeance);
    }
}
