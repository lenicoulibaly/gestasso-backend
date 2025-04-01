package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.archivemodule.controller.repositories.DocumentRepository;
import rigeldevsolutions.gestasso.archivemodule.controller.service.IResourceLoader;
import rigeldevsolutions.gestasso.archivemodule.controller.service.VersementsDocUploader;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.request.UploadDocReq;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.response.ReadDocDTO;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakUserRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.env.KeycloakEnv;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.services.ICotisationService;
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
import rigeldevsolutions.gestasso.reportmodule.service.IReportService;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.MontantConverter;
import rigeldevsolutions.gestasso.sharedmodule.utilities.MontantFormater;
import rigeldevsolutions.gestasso.sharedmodule.utilities.StringUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final VersementsDocUploader versementsDocUploader;
    private final DocumentRepository docRepo;
    private final IReportService reportService;
    private final IResourceLoader resourceLoader;
    private final ICotisationService cotisationService;
    private final KeycloakUserRepo kuRepo;
    private final KeycloakEnv env;

    @Override @Transactional
    public VersementDTO createVersementCotisation(PaiementCotisationDTO dto)
    {
        BigDecimal resteAPayer = calculPaiementService.calculateResteAPayer(dto.getCotisationId(), dto.getAdhesionId());
        if(dto.getMontant().subtract(resteAPayer).compareTo(CINQ)>0) throw new AppException("Le montant du versement ne peut exéder " + MontantFormater.format(resteAPayer));
        Versement versement = paiementMapper.mapToVersement(dto);
        versement.setMontantLettre(MontantConverter.numberToLetter(dto.getMontant()));
        versement.setActive(true);
        versement = versementRepo.save(versement);
        String codeVersement = "VERS-COT-" + versement.getVersementId();
        versement.setCodeVersement(codeVersement);
        BigDecimal montantCotisation = cotisationRepo.getMontantCotisation(dto.getCotisationId()).orElseThrow(()->new AppException("Le montant de la cotisation ne peut être nul"));;
        BigDecimal montantVersement = dto.getMontant();

        final Versement finalVersement = versement;
        ReadEcheanceDTO echeanceActuelle = echeanceService.getCurrentEcheanceToPay(dto.getCotisationId(), dto.getAdhesionId());
        List<PaiementCotisationDTO> paiements = new ArrayList<>();

        if(echeanceActuelle != null )
        {
            BigDecimal montantEcheanceActuelle = echeanceActuelle.getMontantEcheance();

            //Le montant du versement ne permet pas de soldé l'échéance actuelle : il n'y a qu'un seul paiement
            if(montantVersement.compareTo(montantEcheanceActuelle)<=0)
            {
                PaiementCotisationDTO paiement0 = this.createPaiementCotisation(dto, montantVersement, echeanceActuelle.getEcheanceId(), finalVersement);
                paiements.add(paiement0);
            }
            else //Le montant du versement est supérieur au montant de l'échéance actuelle
            {//On fait cas de paiements : solde de l'échéance en cours, paiement des échéances intermédiaires (entiers), dernier paiement
                BigDecimal montantPremierPaiement = echeanceActuelle.getMontantEcheance();
                BigDecimal montantApresPremierPaiement = montantVersement.subtract(montantPremierPaiement);
                PaiementCotisationDTO paiement1 = this.createPaiementCotisation(dto, montantPremierPaiement, echeanceActuelle.getEcheanceId(), finalVersement);
                paiements.add(paiement1);
                int nbrPaiementsEntiers = montantApresPremierPaiement.divideToIntegralValue(montantCotisation).intValue();
                List<ReadEcheanceDTO> echeances = echeanceService.getNextEcheancesToPay(nbrPaiementsEntiers+1, dto.getCotisationId(), dto.getAdhesionId());

                if(echeances != null && echeances.size() >=1)
                {
                    for(int i = 1; i<=nbrPaiementsEntiers; i++)
                    {
                        PaiementCotisationDTO paiement2 = this.createPaiementCotisation(dto, montantCotisation, echeances.get(i).getEcheanceId(), finalVersement);
                        paiements.add(paiement2);
                    }
                }
                BigDecimal montantDernierPaiement = montantApresPremierPaiement.remainder(montantCotisation);
                if(montantDernierPaiement != null && montantDernierPaiement.compareTo(ZERO) > 0)
                {
                    ReadEcheanceDTO lastEcheance = echeanceService.getNextEcheance(echeances.get(echeances.size()-1).getEcheanceId());
                    PaiementCotisationDTO paiement3 = this.createPaiementCotisation(dto, montantDernierPaiement, lastEcheance.getEcheanceId(), finalVersement);
                    paiements.add(paiement3);
                }
            }
        }

        VersementDTO versementDTO = paiementMapper.mapToVersementDto(versement);
        versementDTO.setPaiements(paiements);
        KeycloakUser user = kuRepo.findById(versementDTO.getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable"));
        BeanUtils.copyProperties(user, versementDTO);
        return versementDTO;
    }

    private PaiementCotisationDTO createPaiementCotisation(PaiementCotisationDTO dto, BigDecimal montantPaiement, Long echeanceId, Versement versement) {
        PaiementCotisationDTO paiementDTO = new PaiementCotisationDTO();
        BeanUtils.copyProperties(dto, paiementDTO);
        paiementDTO.setMontant(montantPaiement);

        Paiement paiement = paiementMapper.mapToPaiementCotisation(paiementDTO);
        paiement.setMontantLettre(MontantConverter.numberToLetter(montantPaiement));
        paiement.setActive(true);
        paiement.setEcheance(new Echeance(echeanceId));
        paiement.setVersement(versement);
        paiement = paiementRepo.save(paiement);
        String reference = "PAIE-COT-" + paiement.getPaiementId();
        paiement.setReference(reference);
        paiement = paiementRepo.save(paiement);
        paiementDTO = paiementMapper.mapToPaiementDTO(paiement);
        KeycloakUser user = kuRepo.findById(dto.getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable"));
        BeanUtils.copyProperties(user, paiementDTO);
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
        dto.setMontantLettre(MontantConverter.numberToLetter(dto.getMontant()));
        return dto;
    }

    @Override @Transactional
    public VersementDTO createVersementCotisation(PaiementCotisationDTO dto, List<MultipartFile> files) throws UnknownHostException {
        VersementDTO versement = this.createVersementCotisation(dto);
        for(int i = 0; i< files.size(); i++)
        {
            ReadDocDTO doc = dto.getDocuments().get(i);
            UploadDocReq uploadDocReq = new UploadDocReq(String.valueOf(versement.getVersementId()), doc.getDocUniqueCode(), doc.getDocNum(), doc.getDocName(), doc.getDocDescription(), files.get(i));
            versementsDocUploader.uploadDocument(uploadDocReq);
        }
        return versement;
    }

    @Override // Cette méthode retourne les noms des échéances concernées par un versement
    public String getPeriodesVersement(Long versementId)
    {
        List<String> echeances = paiementRepo.getEcheancesVersement(versementId);
        return String.join(", ", echeances);
    }

    @Override
    public String generateRecuVersement(Long versementId) throws Exception {
        Map<String, Object> parameters = new HashMap<>();

        Long assoId = versementRepo.getAssoIdByVersementId(versementId);
        String qrText = this.generateQrText(versementId);
        parameters.put("VERSEMENT_ID", versementId);

        ReadDocDTO logoDoc = docRepo.getAssoLogo(assoId);
        if(logoDoc != null)
        {
            InputStream logo = resourceLoader.getLocalImages(logoDoc.getDocPath());
            parameters.put("LOGO", logo);
        }

        byte[] bytes = reportService.generateReport("RecuVersement.jrxml", parameters, Collections.EMPTY_LIST, qrText);

        return Base64.getEncoder().encodeToString(bytes);
    }

    @Override
    public Page<VersementDTO> searchVersement(Long cotisationId, LocalDate from, LocalDate to, String key, Pageable pageable)
    {
        Cotisation cotisation = cotisationRepo.findById(cotisationId).orElseThrow(()->new AppException("Cotisation introuvable"));
        from = Optional.ofNullable(from).orElse(cotisation.getDateDebutCotisation());
        to = Optional.ofNullable(to).orElse(cotisationService.getDateFin(cotisation));
        key = Optional.ofNullable(StringUtils.stripAccentsToUpperCase(key)).orElse("");
        List<String> usersIds = kuRepo.searchUserIds(key, env.getKeycloakRealmId());
        Page<VersementDTO> versements = versementRepo.search(cotisationId, from, to, key, usersIds, pageable);
        versements.forEach(v-> {
            v.setMembre(getMembre(v));
            v.setPeriodes(this.getPeriodesVersement(v.getVersementId()));
        });
        return versements;
    }

    @Override
    public List<PaiementCotisationDTO> getPaiementsByVersementId(Long versementId)
    {
        return paiementRepo.getPaiementsByVersementId(versementId);
    }

    @Override
    public List<ReadDocDTO> getDocsByVersementId(Long versementId)
    {
        return docRepo.getDocsByVersementId(versementId);
    }

    private String getMembre(VersementDTO dto)
    {
        String membre = Stream.of(dto.getFirstName(), dto.getLastName(), dto.getMatricule(), dto.getEmail(), dto.getTel()).filter(Objects::nonNull).collect(Collectors.joining("-"));
        return membre;
    }

    private String generateQrText(Long versementId)
    {
        VersementDTO dto = versementRepo.findVersmentById(versementId);
        KeycloakUser user = kuRepo.findById(dto.getUserId()).orElseThrow(()->new AppException("utilisateur introuvable"));
        BeanUtils.copyProperties(user, dto);
        String qrText = "Association : " + dto.getAssoName() + "(" + dto.getAssoSigle() + ")" + "\n";
        qrText = qrText + "Cotisation : " + dto.getNomCotisation() + "\n";
        qrText = qrText + "Motif : " + dto.getMotif()+ "\n";
        qrText = qrText + "Adhérant : " + dto.getFirstName() + " "+ dto.getLastName() +  "\n";
        qrText = qrText + "Matricule : " + dto.getMatricule()+ "\n";
        qrText = qrText + "Email : " + dto.getEmail()+ "\n";
        qrText = qrText + "Tel : " + dto.getTel()+ "\n";

        qrText = qrText + "Montant versé : " + MontantFormater.format(dto.getMontant()) + "(" + dto.getMontantLettre() +")"+ "\n";
        qrText = qrText + "Date de versement : " + dto.getDateVersement()+ "\n";
        qrText = qrText + "Périodes concernées : " + this.getPeriodesVersement(versementId)+ "\n";
        return qrText;
    }
}