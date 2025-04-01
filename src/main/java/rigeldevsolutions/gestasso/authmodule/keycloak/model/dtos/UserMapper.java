package rigeldevsolutions.gestasso.authmodule.keycloak.model.dtos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.env.KeycloakEnv;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.AdhesionDTO;

import java.util.HashMap;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class UserMapper
{
    @Autowired protected KeycloakEnv env;

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "username", source = "email")
    @Mapping(target = "matricule", source = "matriculeFonctionnaire")
    @Mapping(target = "enabled", expression = "java(true)")
    @Mapping(target = "realmId", expression = "java(env.getKeycloakRealm())")
    @Mapping(target = "indice", source = "indiceFonctionnaire")
    public abstract KeycloakUser mapToKeycloakUser(AdhesionDTO dto);

    public Map<String, Object> mapToUserAttributes(AdhesionDTO dto)
    {
        if(dto == null) return new HashMap<>();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("tel", dto.getTel());
        attributes.put("matricule", dto.getMatriculeFonctionnaire());
        attributes.put("lieu_naissance", dto.getLieuNaissance());
        attributes.put("grade_code", dto.getGradeCode());
        attributes.put("indice", dto.getIndiceFonctionnaire());
        attributes.put("code_civilite", dto.getCodeCivilite());
        attributes.put("pays_code", dto.getCodePays());
        attributes.put("date_naissance", dto.getDateNaissance());
        return attributes;
    }
}