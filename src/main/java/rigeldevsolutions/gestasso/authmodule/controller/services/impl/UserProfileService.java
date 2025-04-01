package rigeldevsolutions.gestasso.authmodule.controller.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.UserProfileMappingRepo;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IUserProfileService;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakRoleRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakUserRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.services.IKeycloakApiRoleService;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakRole;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.env.KeycloakEnv;
import rigeldevsolutions.gestasso.authmodule.model.entities.UserProfileMapping;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class UserProfileService implements IUserProfileService
{
    private final UserProfileMappingRepo upmRepo;
    private final KeycloakUserRepo userRepo;
    private final KeycloakRoleRepo roleRepo;
    private final IKeycloakApiRoleService keycloakApiRoleService;
    private final KeycloakEnv env;

    @Override @Transactional
    public void addProfileToUser(String userId, String profileId, Long assoId, Long sectionId)
    {
        checkUserAndProfileExistence(userId, profileId);
        this.checkAssoIdAndSectionIdNullity(assoId, sectionId);
        Optional<UserProfileMapping> userProfileMapping$ = upmRepo.findByUserIdAndProfileId(userId, profileId, assoId, sectionId);

        if(!upmRepo.hasAnyActiveProfile(userId)) //Si l'utilisateur ne possède pas de profile actif, on lui ajoute ce profile comme son profile par défaut
        {
            this.setProfileAsDefaultForUser(userId, profileId, assoId, sectionId);
            return;
        }
        //Si non, on check si l'utilisateur a déjà se profile. Si oui on n'a plus rien a faire. Si non on le lui ajoute simplement
        if(userProfileMapping$.isPresent()) return;
        upmRepo.save(new UserProfileMapping(userId, profileId, assoId, sectionId));
    }

    private void checkUserAndProfileExistence(String userId, String profileId)
    {
        if(userId == null) throw new AppException("L'ID de l'utilisateur est nul");
        if(profileId == null) throw new AppException("L'ID du profile est nul");
        if(!userRepo.existsByUserId(env.keycloakRealm, userId)) throw new AppException("Utilisateur inconnu");
        if(!roleRepo.profileExistsById(env.keycloakClientUuid, profileId)) throw new AppException("Profile inconnu");
    }

    private void checkAssoIdAndSectionIdNullity(Long assoId, Long sectionId)
    {
        if(assoId == null && sectionId == null) throw new AppException("L'ID de l'association et de la section sont tous nuls");
        if(assoId != null && sectionId != null) throw new AppException("L'ID de l'association et de la section sont tous non nuls");
    }

    @Override @Transactional
    public void removeProfileToUser(String userId, String profileId, Long assoId, Long sectionId)
    {
        this.checkUserAndProfileExistence(userId, profileId);
        this.checkAssoIdAndSectionIdNullity(assoId, sectionId);
        upmRepo.removeByUserIdAndProfileId(userId, profileId, assoId, sectionId);
    }

    @Override @Transactional
    public void setProfileAsDefaultForUser(String userId, String profileId, Long assoId, Long sectionId)
    {
        this.checkUserAndProfileExistence(userId, profileId);
        this.checkAssoIdAndSectionIdNullity(assoId, sectionId);
        KeycloakRole oldActiveRole = this.getUserActiveProfile(userId);
        KeycloakRole newActiveRole = roleRepo.findById(profileId).orElseThrow(()->new AppException("Profile introuvable"));
        upmRepo.setOtherAsNoneActive(userId, profileId);
        Optional<UserProfileMapping> userProfileMapping$ = upmRepo.findByUserIdAndProfileId(userId, profileId, assoId, sectionId);
        if(userProfileMapping$.isPresent())
        {
            UserProfileMapping userProfileMapping = userProfileMapping$.get();
            userProfileMapping.setActive(true);
        }
        else
        {
            UserProfileMapping userProfileMapping = new UserProfileMapping(null, userId, profileId, assoId, sectionId,true);
            userProfileMapping = upmRepo.save(userProfileMapping);
        }
        //Quand on ajoute un profile en tant que profile par défaut, il faut supprimer coté keycloak l'ancien profile et ajouter le nouveau
        keycloakApiRoleService.removeProfileToUser(oldActiveRole, userId);
        keycloakApiRoleService.addProfileToUser(newActiveRole, userId);
    }

    @Override
    public KeycloakRole getUserActiveProfile(String userId)
    {
        UserProfileMapping upm = upmRepo.findActiveByUser(userId);
        KeycloakRole role = roleRepo.findById(upm.getProfileId()).orElse(null);
        return role;
    }
}
