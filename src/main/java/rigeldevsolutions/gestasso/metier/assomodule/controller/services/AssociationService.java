package rigeldevsolutions.gestasso.metier.assomodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.archivemodule.controller.repositories.DocumentRepository;
import rigeldevsolutions.gestasso.archivemodule.controller.service.AbstractDocumentService;
import rigeldevsolutions.gestasso.archivemodule.controller.service.AssociationDocUploader;
import rigeldevsolutions.gestasso.archivemodule.controller.service.IResourceLoader;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.request.UploadDocReq;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.response.ReadDocDTO;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.AssoRepo;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.UpdateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.mappers.AssoMapper;
import rigeldevsolutions.gestasso.reportmodule.service.IReportService;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.StringUtils;

import java.io.InputStream;
import java.util.*;

@Service @RequiredArgsConstructor
public class AssociationService implements IAssociationService
{
    private final AssoRepo assoRepo;
    private final AssoMapper assoMapper;
    private final AssociationDocUploader associationDocUploader;
    private final ISectionService sectionService;
    private final DocumentRepository docRepo;
    private final AbstractDocumentService documentService;
    private final IReportService reportService;
    private final IResourceLoader resourceLoader;

    @Override @Transactional
    public Association createAssociation(CreateAssociationDTO dto)
    {
        Association association = assoMapper.mapToAssociation(dto);
        association = assoRepo.save(association);
        Long assoId = association.getAssoId();

        List<CreateSectionDTO> createSectionDTOS = dto.getCreateSectionDTOS();

        if(createSectionDTOS == null || createSectionDTOS.isEmpty())
        {
            sectionService.createSectionDeBase(association);
        }
        else
        {
            createSectionDTOS.stream()
                    .filter(Objects::nonNull)
                    .forEach(createSectionDTO->
                    {
                        createSectionDTO.setAssoId(assoId);
                        sectionService.createSection(createSectionDTO);
                    });
        }

       return association;
    }

    @Override @Transactional
    public Association updateAssociation(UpdateAssociationDTO dto)
    {
        Association association = assoRepo.findById(dto.getAssoId()).orElseThrow(()->new AppException("Association introuvable"));

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
        Page<ReadAssociationDTO> assoPage = assoRepo.searchAssociations(key, pageable);
        List<ReadAssociationDTO> assoList = assoPage.toList();
        assoList.forEach(a->
        {
            ReadDocDTO logo = docRepo.getAssoLogo(a.getAssoId());
            if(logo != null) logo.setFile(documentService.downloadFile(logo.getDocPath()));
            a.setLogo(logo);
        });
        return new PageImpl<>(assoList, assoPage.getPageable(), assoPage.getTotalElements());
    }

    @Override
    public ReadAssociationDTO findById(Long assoId)
    {
        if(assoId == null) throw new AppException("L'ID de l'association ne peut être nul");
        ReadAssociationDTO assoDTO = assoRepo.findReadAssoDtoById(assoId);
        ReadDocDTO logo = docRepo.getAssoLogo(assoId);
        assoDTO.setLogo(logo);
        return assoDTO;
    }

    @Override
    public Association createAssociation(CreateAssociationDTO dto, MultipartFile logo)
    {
        Association association = this.createAssociation(dto);
        UploadDocReq uploadDocReq = new UploadDocReq(String.valueOf(association.getAssoId()), "LOGO", null, "logo_"+dto.getSigle(), "logo", logo);
        associationDocUploader.uploadDocument(uploadDocReq);
        return association;
    }

    @Override
    public byte[] generateFicheAdhesion(Long assoId) throws Exception {
        Map<String, Object> parameters = new HashMap<>();

        String assoSigle = assoRepo.getSigleByAssoId(assoId);
        String qrText = "Fiche d'adhésion " + assoSigle;
        parameters.put("ASSO_ID", assoId);

        ReadDocDTO logoDoc = docRepo.getAssoLogo(assoId);
        if(logoDoc != null)
        {
            InputStream logo = resourceLoader.getLocalImages(logoDoc.getDocPath());
            parameters.put("LOGO", logo);
        }

        return reportService.generateReport("FicheAdhesion.jrxml", parameters, Collections.EMPTY_LIST, qrText);
    }
}