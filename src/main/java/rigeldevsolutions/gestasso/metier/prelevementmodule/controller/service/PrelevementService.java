package rigeldevsolutions.gestasso.metier.prelevementmodule.controller.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.archivemodule.controller.service.PrelevementsDocUploader;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.request.UploadDocReq;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.response.ReadDocDTO;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakUserRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.services.IEcheanceService;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO;
import rigeldevsolutions.gestasso.metier.prelevementmodule.controller.repositories.DefautPrelevementRepo;
import rigeldevsolutions.gestasso.metier.prelevementmodule.controller.repositories.PrelevementRepo;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.DefautPrelevementDTO;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.PrelevementDTO;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.entities.DefautPrelevement;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.entities.Prelevement;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.mappers.PrelevementMapper;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.MontantConverter;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

@Service @RequiredArgsConstructor @Validated
public class PrelevementService implements IPrelevementService
{
    private final PrelevementRepo prelevementRepo;
    private final DefautPrelevementRepo defautPrelevementRepo;
    private final PrelevementMapper prelevementMapper;
    private final PrelevementsDocUploader prelevementsDocUploader;
    private final IEcheanceService echeanceService;
    private final CotisationRepo cotisationRepo;
    private final KeycloakUserRepo kuRepo;

    @Override
    public PrelevementDTO getPrelevementEditDto(PrelevementDTO dto)
    {
        if(dto == null) throw new AppException("Aucune donnée de prélèvement parvenue");
        Long cotisationId = dto.getCotisationId();
        Cotisation cotisation = cotisationRepo.findById(cotisationId).orElseThrow(()->new AppException("Cotisation introuvable " + cotisationId));
        BigDecimal motantCotisation = cotisation.getMontantCotisation();
        BigDecimal montantPrelevement = motantCotisation.multiply(new BigDecimal(dto.getNbrAdherant()));
        String montantPrelevementLettre = MontantConverter.numberToLetter(montantPrelevement);

        PrelevementDTO editDto = prelevementRepo.getPrelevementEditDto(cotisationId);
        editDto.setNbrAdherant(dto.getNbrAdherant());
        editDto.setMontant(montantPrelevement);
        editDto.setMontantLettre(montantPrelevementLettre);
        ReadEcheanceDTO currentEcheance = echeanceService.getCurrentEcheanceToPay(cotisationId);
        editDto.setEcheanceId(currentEcheance.getEcheanceId());
        editDto.setNomEcheance(currentEcheance.getNomEcheance());
        editDto.setDefautPrelevements(Collections.emptyList());
        editDto.setDocs(Collections.emptyList());
        return editDto;
    }

    @Override
    public PrelevementDTO savePrelevement(@Valid PrelevementDTO dto, List<MultipartFile> files)
    {
        if(dto == null) throw new AppException("Aucune donnée parvenue");
        if(dto.getPrelevementId() == null) return this.create(dto, files);
        return this.update(dto);
    }

    @Override
    public Page<PrelevementDTO> searchPrelevementsCotisation(Long cotisationId, String key, Pageable pageable)
    {
        Page<PrelevementDTO> prelevementDTOS = prelevementRepo.searchPrelevements(cotisationId, key, pageable);
        return prelevementDTOS;
    }

    @Override
    public Page<DefautPrelevementDTO> getDefautPrelevementCotisationPage(Long prelevementId, Pageable pageable)
    {
        Page<DefautPrelevementDTO> defautPrelevementDTOS = defautPrelevementRepo.getDefautPrelevemenListByPrelevementId(prelevementId, pageable);
        if(defautPrelevementDTOS == null || defautPrelevementDTOS.isEmpty()) return Page.empty();
        List<DefautPrelevementDTO> defautPrelevementList = defautPrelevementDTOS.stream().peek(dto->{
            KeycloakUser user = kuRepo.findById(dto.getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable"));
            BeanUtils.copyProperties(user, dto);
        }).collect(Collectors.toList());

        return new PageImpl<>(defautPrelevementList, pageable, defautPrelevementDTOS.getTotalElements());
    }

    @Override
    public DefautPrelevementDTO saveDefautPrelevementCotisation(DefautPrelevementDTO dto)
    {
        if(dto == null) return null;
        if(dto.getDefautPrelevementId() == null) return this.createDefautPrelevement(dto);
        return this.updateDefautPrelevement(dto);
    }

    private DefautPrelevementDTO updateDefautPrelevement(DefautPrelevementDTO dto)
    {
        if(dto == null) return null;
        DefautPrelevement defautPrelevement = defautPrelevementRepo.findById(dto.getDefautPrelevementId()).orElseThrow(()->new AppException("Defaut de prélèvement introuvable"));
        defautPrelevement.setActive(dto.isActive());
        defautPrelevement.setMotif(Optional.ofNullable(dto.getMotifDefaut()).orElse(defautPrelevement.getMotif()));
        DefautPrelevementDTO output = defautPrelevementRepo.getDefautPrelevementDtoById(defautPrelevement.getDefautPrelevementId());
        KeycloakUser user = kuRepo.findById(dto.getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable"));
        BeanUtils.copyProperties(user, output);
        return output;
    }

    private DefautPrelevementDTO createDefautPrelevement(DefautPrelevementDTO dto)
    {
        DefautPrelevement defautPrelevement = prelevementMapper.mapToDefautPrelevement(dto);
        BigDecimal montantCotisation = prelevementRepo.getMontantCotisationByPrelevementId(dto.getPrelevementId()).orElse(ZERO);
        String montantLettre = MontantConverter.numberToLetter(montantCotisation);
        defautPrelevement.setMontant(montantCotisation);
        defautPrelevement.setMontantLettre(montantLettre);
        defautPrelevement = defautPrelevementRepo.save(defautPrelevement);
        DefautPrelevementDTO output = defautPrelevementRepo.getDefautPrelevementDtoById(defautPrelevement.getDefautPrelevementId());
        KeycloakUser user = kuRepo.findById(dto.getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable"));
        BeanUtils.copyProperties(user, output);
        return output;
    }

    private PrelevementDTO create(PrelevementDTO dto, List<MultipartFile> files)
    {
        Prelevement prelevement = prelevementMapper.mapToPrelevementCotisation(dto);
        prelevement.setMontantLettre(MontantConverter.numberToLetter(dto.getMontant()));
        prelevement = prelevementRepo.save(prelevement);
        Long prelevementId = prelevement.getPrelevementId();
        List<ReadDocDTO> docs = dto.getDocs();
        if(docs != null && !docs.isEmpty())
        {
            for(int i = 0; i<files.size(); i++)
            {
                ReadDocDTO doc = docs.get(i);
                UploadDocReq uploadDocReq = new UploadDocReq(String.valueOf(prelevementId), doc.getDocUniqueCode(), doc.getDocNum(), doc.getDocName(), doc.getDocDescription(), files.get(i));
                prelevementsDocUploader.uploadDocument(uploadDocReq);
            }
        }
        return saveDefautPrelevements(dto.getDefautPrelevements(), prelevementId);
    }

    private PrelevementDTO update(PrelevementDTO dto)
    {
        if(dto.getPrelevementId() == null) throw new AppException("Veuillez sélectionner le prélèvement");
        Prelevement prelevement = prelevementRepo.findById(dto.getPrelevementId()).orElseThrow(()->new AppException("Prélèvement introuvable"));

        prelevement.setMontant(dto.getMontant());
        prelevement.setMontantLettre(MontantConverter.numberToLetter(dto.getMontant()));
        prelevement.setNbrAdherant(dto.getNbrAdherant());
        prelevement = prelevementRepo.save(prelevement);
        return saveDefautPrelevements(dto.getDefautPrelevements(), prelevement.getPrelevementId());
    }


    private PrelevementDTO saveDefautPrelevements(List<DefautPrelevementDTO> defautPrelevementsInput, Long prelevementId)
    {

        List<DefautPrelevementDTO> defautPrelevementsOutput = new ArrayList<>();
        if(defautPrelevementsInput != null && !defautPrelevementsInput.isEmpty())
        {
            defautPrelevementsOutput = defautPrelevementsInput.stream().peek(pd->pd.setPrelevementId(prelevementId)).map(dp->this.saveDefautPrelevementCotisation(dp)).filter(Objects::nonNull).collect(Collectors.toList());
        }
        PrelevementDTO prelevementDTO = prelevementRepo.findPrelevementDtoById(prelevementId);
        prelevementDTO.setDefautPrelevements(defautPrelevementsOutput);
        return prelevementDTO;
    }
}
