package rigeldevsolutions.gestasso.metier.assomodule.controller.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.UpdateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;

import java.net.UnknownHostException;

public interface IAssociationService
{
    Association createAssociation(CreateAssociationDTO dto);
    Association updateAssociation(UpdateAssociationDTO dto);
    Page<ReadAssociationDTO> searchAssociations(String key, Pageable pageable);

    ReadAssociationDTO findById(Long assoId);

    Association createAssociation(CreateAssociationDTO dto, MultipartFile logo) throws UnknownHostException;
    byte[] generateFicheAdhesion(Long assoId) throws Exception;
}
