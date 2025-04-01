package rigeldevsolutions.gestasso.metier.assomodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IUserProfileService;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakUserRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.services.KeycloakApiUserService;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.dtos.ProfileDto;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.dtos.UserMapper;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.env.KeycloakEnv;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.AdhesionRepo;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.AdhesionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section;
import rigeldevsolutions.gestasso.metier.assomodule.model.mappers.AdhesionMapper;
import rigeldevsolutions.gestasso.sharedmodule.dtos.SelectOption;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class AdhesionService implements IAdhesionService
{
    private final AdhesionMapper adhesionMapper;
    private final AdhesionRepo adhesionRepo;
    private final KeycloakUserRepo kuRepo;
    private final UserMapper userMapper;
    private final KeycloakApiUserService keycloakApiUserService;
    private final IUserProfileService userProfileService;
    private final KeycloakEnv env;

    @Override @Transactional
    public Adhesion createUserAndAdhesion(AdhesionDTO dto)
    {
        if(kuRepo.existsByEmail(dto.getEmail())) throw new AppException("Email déjà attribué " + dto.getEmail());
        if(kuRepo.existsByTel(dto.getTel())) throw new AppException("N° téléphone déjà attribué " + dto.getTel());
        if(kuRepo.existsByMatricule(dto.getMatriculeFonctionnaire())) throw new AppException("Matricule déjà attribué " + dto.getMatriculeFonctionnaire());
        if(dto.getAssoId() == null) throw new AppException("Veuillez sélectionner l'association");
        KeycloakUser user = userMapper.mapToKeycloakUser(dto);
        Map<String, Object> attributes = userMapper.mapToUserAttributes(dto);
        user.setAttributes(attributes);
        user = keycloakApiUserService.addUser(user);
        String userId = user.getId();
        Adhesion adhesion = new Adhesion(null, new Association(dto.getAssoId()), dto.getSectionId() == null ? null : new Section(dto.getSectionId()), true, userId, null);
        adhesion = adhesionRepo.save(adhesion);
        dto.getProfileDtos().stream()
                .sorted(Comparator.comparing(ProfileDto::isActive).reversed()) //Mettre le profile active en tete
                .forEach(p->userProfileService.addProfileToUser(userId, p.getId(), dto.getAssoId(), dto.getSectionId()));
        return adhesion;
    }

    @Override ///@Transactional
    public Adhesion updateMembre(AdhesionDTO dto)
    {
        Adhesion adhesion = adhesionRepo.findById(dto.getAdhesionId()).orElseThrow(()->new AppException("Membre introuvable " + dto.getAdhesionId()));
        KeycloakUser user = kuRepo.findById(dto.getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable " + dto.getUserId()));
        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setMatricule(dto.getMatriculeFonctionnaire());
        user.setTel(dto.getTel());
        user.setCodeCivilite(dto.getCodeCivilite());
        user.setDateNaissance(dto.getDateNaissance());
        user.setLieuNaissance(dto.getLieuNaissance());
        user = kuRepo.save(user);
        adhesion.setSection(new Section(dto.getSectionId()));
        return adhesionRepo.save(adhesion);
    }

    @Override @Transactional
    public void seDesabonner(Long adhesionId)
    {
        Adhesion adhesion = adhesionRepo.findById(adhesionId).orElseThrow(()-> new AppException("Adhésion introuvable"));
        adhesion.setActive(false);
    }

    @Override
    public Page<AdhesionDTO> searchAdhsions(String key, Long assoId, Long sectionId, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        List<String> usersIds = keycloakApiUserService.searchUsersIds(key);
        return adhesionRepo.searchAdhsions(key, usersIds, assoId, sectionId, pageable);
    }

    @Override
    public AdhesionDTO getMembreDTO(String uniqueIdentifier)
    {
        uniqueIdentifier = Optional.ofNullable(uniqueIdentifier).orElse("{#}") ;
        KeycloakUser keycloakUser = kuRepo.findByIdIdentifiant(uniqueIdentifier);
        AdhesionDTO dto = adhesionMapper.mapToAdhesionDto(keycloakUser);
        return dto;
    }

    @Override
    public List<SelectOption> getOptions(Long assoId)
    {
        if(assoId == null) return Collections.emptyList();
        List<Adhesion> adhesions = adhesionRepo.getAdhesionsByAssoId(assoId);
        if(adhesions == null || adhesions.isEmpty()) return Collections.emptyList();
        List<SelectOption> selectOptions = adhesions.stream()
                .peek(a->a.setKeycloakUser(kuRepo.findById(a.getUserId()).orElse(null)))
                .map(a->new SelectOption(a.getAdhesionId(), a.getKeycloakUser().toString()))
                .collect(Collectors.toList());
        return selectOptions;
    }



    @Override
    public Optional<Adhesion> findByEmailAndSection(String email, Long sectionId)
    {
        String userId = kuRepo.findUserIdByEmail(email, env.getKeycloakRealmId());
        return adhesionRepo.findByUserIdAndSectionId(userId, sectionId);
    }

    @Override
    public Optional<Adhesion> findByEmailAndAsso(String email, Long assoId)
    {
        String userId = kuRepo.findUserIdByEmail(email, env.getKeycloakRealmId());
        return adhesionRepo.findByUserIdAndAsso(userId, assoId);
    }
}