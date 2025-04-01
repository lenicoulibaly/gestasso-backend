package rigeldevsolutions.gestasso.metier.assomodule.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakUserRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.AdhesionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;

@Mapper(componentModel = "spring")
public abstract class AdhesionMapper
{
    @Autowired protected KeycloakUserRepo keycloakUserRepo;
    @Mapping(target = "association", expression = "java(dto.getAssoId() == null ? null : new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association(dto.getAssoId()))")
    @Mapping(target = "section", expression = "java(dto.getSectionId() == null ? null : new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section(dto.getSectionId()))")
    @Mapping(target = "keycloakUser", expression = "java(dto.getUserId() == null ? null : keycloakUserRepo.findById(dto.getUserId()).orElseThrow(()->new rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException(\"Utilisateur introuvable\")))")
    @Mapping(target = "active", expression = "java(true)")
    public abstract Adhesion mapToAdhesion(AdhesionDTO dto);

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "matriculeFonctionnaire", source = "matricule")
    @Mapping(target = "indiceFonctionnaire", source = "indice")
    public abstract AdhesionDTO mapToAdhesionDto(KeycloakUser user);
}
