package rigeldevsolutions.gestasso.archivemodule.model.dtos;

import rigeldevsolutions.gestasso.archivemodule.model.dtos.request.UploadDocReq;
import rigeldevsolutions.gestasso.archivemodule.model.entities.Document;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DocMapper
{
    @Autowired protected TypeRepo typeRepo;

    @Mapping(target = "docDescription", expression = "java(\"Photo de profil\")")
    @Mapping(target = "docType", expression = "java(typeRepo.findById(dto.getDocUniqueCode()).orElseThrow(()->new rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "user", expression = "java(new rigeldevsolutions.gestasso.authmodule.model.entities.AppUser(dto.getObjectId()))")
    @Mapping(target = "file", expression = "java(rigeldevsolutions.gestasso.sharedmodule.utilities.Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), dto.getExtension()))")
    public abstract Document mapToPhotoDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findById(dto.getDocUniqueCode()).orElseThrow(()->new rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "association", expression = "java(new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association(dto.getObjectId()))")
    @Mapping(target = "file", expression = "java(rigeldevsolutions.gestasso.sharedmodule.utilities.Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), dto.getExtension()))")
    public abstract Document mapToAssociationDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findById(dto.getDocUniqueCode()).orElseThrow(()->new rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "section", expression = "java(new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section(dto.getObjectId()))")
    @Mapping(target = "file", expression = "java(rigeldevsolutions.gestasso.sharedmodule.utilities.Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), dto.getExtension()))")
    public abstract Document mapToSectionDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findById(dto.getDocUniqueCode()).orElseThrow(()->new rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "user", expression = "java(new rigeldevsolutions.gestasso.authmodule.model.entities.AppUser(dto.getObjectId()))")
    @Mapping(target = "file", expression = "java(rigeldevsolutions.gestasso.sharedmodule.utilities.Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), dto.getExtension()))")
    public abstract Document mapToMembreDoc(UploadDocReq dto);
}