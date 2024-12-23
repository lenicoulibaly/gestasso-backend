package rigeldevsolutions.gestasso.metier.assomodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.UserRepo;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IUserService;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appuser.ReadUserDTO;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.MembreRepo;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateMembreDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadMembreDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section;
import rigeldevsolutions.gestasso.metier.assomodule.model.mappers.MembreMapper;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.StringUtils;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class MembreService implements IMembreService
{
    private final MembreMapper membreMapper;
    private final MembreRepo membreRepo;
    private final IUserService userService;
    private final UserRepo userRepo;
    @Override @Transactional
    public Adhesion createMembre(CreateMembreDTO dto, ActionIdentifier ai)
    {
        Optional<Adhesion> adhesion$ = membreRepo.findByEmailAndSection(dto.getEmail(), dto.getSectionId());
        if(adhesion$.isPresent()) return adhesion$.get();
        adhesion$ = membreRepo.findByEmailAndAsso(dto.getEmail(), dto.getAssoId());
        if(adhesion$.isPresent())
        {
            Adhesion adhesion = adhesion$.get();
            adhesion.setSection(new Section(dto.getSectionId()));
            ai.setActionName("Changement de section");
            BeanUtils.copyProperties(ai, adhesion);
            return membreRepo.save(adhesion);
        }
        ReadUserDTO user = userService.createAdherant(dto, ai);
        dto.setUserId(user.getUserId());
        Adhesion adhesion = membreMapper.mapToAdhesion(dto);
        BeanUtils.copyProperties(ai, adhesion);
        return membreRepo.save(adhesion);
    }

    @Override @Transactional
    public Adhesion updateMembre(ReadMembreDTO dto, ActionIdentifier ai)
    {

        Adhesion adhesion = membreRepo.findById(dto.getAdhesionId()).orElseThrow(()->new AppException("Membre introuvable " + dto.getAdhesionId()));
        AppUser user = userRepo.findById(dto.getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable " + dto.getUserId()));
        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setMatriculeFonctionnaire(dto.getMatriculeFonctionnaire());
        user.setTel(dto.getTel());
        user.setCivilite(new Type(dto.getCodeCivilite()));
        user.setDateNaissance(dto.getDateNaissance());
        user.setLieuNaissance(dto.getLieuNaissance());
        user = userRepo.save(user);
        adhesion.setSection(new Section(dto.getSectionId()));

        BeanUtils.copyProperties(ai, user);
        BeanUtils.copyProperties(ai, adhesion);
        return membreRepo.save(adhesion);
    }

    @Override @Transactional
    public void seDesabonner(Long adhesionId, ActionIdentifier ai)
    {
        Adhesion adhesion = membreRepo.findById(adhesionId).orElseThrow(()-> new AppException("Adhésion introuvable"));
        adhesion.setActive(false);
        BeanUtils.copyProperties(ai, adhesion);
    }

    @Override
    public Page<ReadMembreDTO> searchMembers(String key, Long assoId, Long sectionId, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        return membreRepo.searchMembers(key, assoId, sectionId, pageable);
    }

    @Override
    public CreateMembreDTO getMembreDTO(String uniqueIdentifier)
    {
        uniqueIdentifier = Optional.ofNullable(uniqueIdentifier).orElse("{#}") ;
        CreateMembreDTO dto = membreRepo.findByIdIdentifiant(uniqueIdentifier);
        return dto;
    }
}