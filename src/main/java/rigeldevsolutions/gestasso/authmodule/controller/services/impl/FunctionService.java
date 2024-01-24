package rigeldevsolutions.gestasso.authmodule.controller.services.impl;

import rigeldevsolutions.gestasso.authmodule.controller.repositories.FunctionRepo;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.RoleToFunctionAssRepo;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.UserRepo;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IFunctionService;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IJwtService;
import rigeldevsolutions.gestasso.authmodule.model.constants.AuthActions;
import rigeldevsolutions.gestasso.authmodule.model.constants.AuthTables;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction.CreateFncDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction.FncMapper;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction.ReadFncDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction.UpdateFncDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appuser.AuthResponseDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.asignation.AssMapper;
import rigeldevsolutions.gestasso.authmodule.model.dtos.asignation.RoleAssSpliterDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.asignation.SetAuthoritiesToFunctionDTO;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppFunction;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppRole;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;
import rigeldevsolutions.gestasso.authmodule.model.entities.RoleToFncAss;
import rigeldevsolutions.gestasso.modulelog.controller.service.ILogService;
import rigeldevsolutions.gestasso.modulelog.model.entities.Log;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.ObjectCopier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FunctionService implements IFunctionService {

    @Lazy @Autowired private UserDetailsService uds;

    private final FunctionRepo functionRepo;
    private final RoleToFunctionAssRepo rtfRepo;
    private final UserRepo userRepo;
    private final FncMapper fncMapper;
    private final AssMapper assMapper;
    private final ILogService logger;
    private final ObjectCopier<AppFunction> functionCopier;
    private final ObjectCopier<AppUser> userCopier;
    private final ObjectCopier<RoleToFncAss> rtfCopier;
    private final IJwtService jwtService;

    @Override
    public Long getActiveCurrentFunctionId(Long userId)
    {
        Set<Long> ids = functionRepo.getCurrentFncIds(userId);
        return ids == null ? null : ids.size() != 1 ? null : new ArrayList<>(ids).get(0);
    }

    @Override
    public String getActiveCurrentFunctionName(Long userId) {
        Set<String> fncNames = functionRepo.getCurrentFncNames(userId);
        return fncNames == null ? null : fncNames.size() != 1 ? null : new ArrayList<>(fncNames).get(0);
    }

    @Override @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public ReadFncDTO createFnc(CreateFncDTO dto) throws UnknownHostException
    {
        AppFunction function = fncMapper.mapToFunction(dto);
        boolean userHasFunction = functionRepo.userHasAnyAppFunction(dto.getUserId());
        function = functionRepo.save(function);
        Log mainLog = logger.logg(AuthActions.CREATE_FNC, null, function, AuthTables.FUNCTION, null);
        Long fncId = function.getId();
        if(!userHasFunction)
        {
            function.setFncStatus(1);
            AppUser user = userRepo.findById(dto.getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable"));
            AppUser oldUser = userCopier.copy(user);
            user.setCurrentFunctionId(fncId);
            user = userRepo.save(user);
            logger.logg(AuthActions.SET_USER_DEFAULT_FNC_ID, oldUser, user, AuthTables.USER_TABLE, mainLog.getId());
        }
        Set<String> roleCodes = dto.getRoleCodes() == null ? new HashSet<>() : dto.getRoleCodes();

        roleCodes.forEach(code->
        {
            RoleToFncAss roleToFunctionAss = new RoleToFncAss();
            roleToFunctionAss.setAssStatus(1);
            roleToFunctionAss.setStartsAt(dto.getStartsAt());
            roleToFunctionAss.setEndsAt(dto.getEndsAt());
            roleToFunctionAss.setRole(new AppRole(code));
            roleToFunctionAss.setFunction(new AppFunction(fncId));
            roleToFunctionAss = rtfRepo.save(roleToFunctionAss);
            try {
                logger.logg(AuthActions.ADD_ROLE_TO_FNC, null, roleToFunctionAss, AuthTables.ASS, mainLog.getId());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        return fncMapper.mapToReadFncDto(function);
    }

    @Override @Transactional
    public AuthResponseDTO setFunctionAsDefault(Long fncId) throws UnknownHostException
    {
        AppFunction function  = functionRepo.findById(fncId).orElseThrow(()->new AppException("Fonction inconnue"));
        if(function.getUser() == null ||function.getUser().getUserId() == null) throw new AppException("Utilisateur introuvable pour cette fonction");
        AppUser user = userRepo.findById(function.getUser().getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable pour cette fonction"));
        UserDetails userDetails = uds.loadUserByUsername(user.getEmail());
        if(function.getFncStatus() == 1)
        {
            return jwtService.generateJwt(userDetails, jwtService.extractConnectionId());
        }
        AppFunction oldFnc = functionCopier.copy(function);
        function.setFncStatus(1);
        Log mainLog = logger.logg(AuthActions.SET_FNC_AS_DEFAULT, oldFnc, function, AuthTables.FUNCTION, null);

        functionRepo.findActiveByUser(function.getUser().getUserId()).forEach(fnc->
        {
            if(!fnc.getId().equals(fncId) && fnc.getFncStatus() == 1)
            {
                AppFunction oldFnc1 = functionCopier.copy(fnc);
                fnc.setFncStatus(2);
                fnc = functionRepo.save(fnc);
                try
                {
                    logger.logg(AuthActions.SET_FNC_AS_NONE_DEFAULT, oldFnc1, fnc, AuthTables.FUNCTION, mainLog.getId());
                } catch (UnknownHostException e)
                {
                    e.printStackTrace();
                }
            }
        });

        AppUser oldUser = userCopier.copy(user);
        user.setCurrentFunctionId(fncId);
        user = userRepo.save(user);
        logger.logg(AuthActions.SET_USER_DEFAULT_FNC_ID, oldUser, user, AuthTables.USER_TABLE, mainLog.getId());
        return jwtService.generateJwt(userDetails, UUID.randomUUID().toString());
    }

    @Override @Transactional
    public void revokeFunction(Long fncId) throws UnknownHostException {
        AppFunction function  = functionRepo.findById(fncId).orElse(null);
        if(function == null) return;
        if(function.getFncStatus() == 3) return;
        AppFunction oldFnc = functionCopier.copy(function);
        function.setFncStatus(3); functionRepo.save(function);
        logger.logg(AuthActions.REVOKE_FNC, oldFnc, function, AuthTables.FUNCTION, null);
    }

    @Override @Transactional
    public void restoreFunction(Long fncId) throws UnknownHostException
    {
        AppFunction function  = functionRepo.findById(fncId).orElse(null);
        if(function == null) return;
        if(function.getFncStatus() == 1 || function.getFncStatus() == 2) return;
        AppFunction oldFnc = functionCopier.copy(function);
        function.setFncStatus(2); functionRepo.save(function);
        logger.logg(AuthActions.REVOKE_FNC, oldFnc, function, AuthTables.FUNCTION, null);
    }

    //@Transactional
    private ReadFncDTO setFunctionAuthorities(SetAuthoritiesToFunctionDTO dto)
    {
        AppFunction function  = functionRepo.findById(dto.getFncId()).orElse(null);
        if(function == null) return null;
        Long fncId = function.getId();
        Set<String> roleCodes = dto.getRoleCodes();
        LocalDate startsAt = dto.getStartsAt(); LocalDate endsAt = dto.getEndsAt();
        RoleAssSpliterDTO roleAssSpliterDTO = assMapper.mapToRoleAssSpliterDTO(roleCodes, fncId, startsAt, endsAt, true);
        treatRolesAssignation(roleAssSpliterDTO, fncId, startsAt, endsAt);

        return fncMapper.mapToReadFncDto(function);
    }

    @Override @Transactional
    public ReadFncDTO updateFunction(UpdateFncDTO dto) throws UnknownHostException {
        AppFunction function  = functionRepo.findById(dto.getFncId()).orElse(null);
        AppFunction oldFunction = functionCopier.copy(function);
        if(function == null) return null;
        function.setName(dto.getName());
        function.setStartsAt(dto.getStartsAt());
        function.setEndsAt(dto.getEndsAt());
        logger.logg(AuthActions.UPDATE_FUNCTION, oldFunction, function, AuthTables.FUNCTION, null);
        ReadFncDTO readFncDTO = this.setFunctionAuthorities(new SetAuthoritiesToFunctionDTO(dto.getFncId(),dto.getStartsAt(), dto.getEndsAt(), dto.getRoleCodes()));
        return readFncDTO;
    }

    @Override
    public ReadFncDTO getActiveCurrentFunction(Long userId)
    {
        Long currentFncId = this.getActiveCurrentFunctionId(userId);
        if(currentFncId == null) return null;
        AppFunction function = functionRepo.findById(currentFncId).orElseThrow(()->new AppException("Fonction introuvable"));
        ReadFncDTO readFncDTO = fncMapper.mapToReadFncDto(function);
        return readFncDTO;
    }

    @Override
    public ReadFncDTO getFunctioninfos(Long foncId)
    {
        AppFunction function = functionRepo.findById(foncId).orElseThrow(()-> new AppException("Fonction introuvable"));
        ReadFncDTO readFncDTO = fncMapper.mapToReadFncDto(function);
        return readFncDTO;
    }

    private void treatRolesAssignation(RoleAssSpliterDTO roleAssSpliterDTO, Long fncId, LocalDate startsAt, LocalDate endsAt)
    {
        roleAssSpliterDTO.getRoleCodesToBeRemoved().forEach(code->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, code);
            RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
            rtfAss.setAssStatus(2);
            rtfAss = rtfRepo.save(rtfAss);
            try {
                logger.logg(AuthActions.REMOVE_ROLE_TO_FNC, oldRtfAss, rtfAss, AuthTables.ASS, null);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        roleAssSpliterDTO.getRoleCodesToBeAddedAsNew().forEach(code->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, code);
            if(rtfAss == null)
            {
                rtfAss = new RoleToFncAss();
                rtfAss.setAssStatus(1); rtfAss.setStartsAt(startsAt); rtfAss.setEndsAt(endsAt);
                rtfAss.setRole(new AppRole(code));
                rtfAss.setFunction(new AppFunction(fncId));
                rtfRepo.save(rtfAss);
                try {
                    logger.logg(AuthActions.ADD_ROLE_TO_FNC, null, rtfAss, AuthTables.ASS, null);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
                rtfAss.setAssStatus(1);
                rtfRepo.save(rtfAss);
                try {
                    logger.logg(AuthActions.RESTORE_ROLE_TO_FNC, oldRtfAss, rtfAss, AuthTables.ASS, null);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });

        roleAssSpliterDTO.getRoleCodesToChangeTheDates().forEach(id->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, id);
            RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
            rtfAss.setStartsAt(startsAt); rtfAss.setEndsAt(endsAt);
            rtfAss = rtfRepo.save(rtfAss);
            try {
                logger.logg(AuthActions.CHANGE_ROLE_TO_FNC_VALIDITY_PERIOD, oldRtfAss, rtfAss, AuthTables.ASS, null);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        roleAssSpliterDTO.getRoleCodesToActivate().forEach(id->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, id);
            RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
            rtfAss.setAssStatus(1);
            rtfAss = rtfRepo.save(rtfAss);
            try {
                logger.logg(AuthActions.RESTORE_ROLE_TO_FNC, oldRtfAss, rtfAss, AuthTables.ASS, null);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        roleAssSpliterDTO.getRoleCodesToActivateAndChangeTheDates().forEach(id->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, id);
            RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
            rtfAss.setAssStatus(1);rtfAss.setStartsAt(startsAt); rtfAss.setEndsAt(endsAt);
            rtfAss = rtfRepo.save(rtfAss);
            try {
                logger.logg(AuthActions.ASSIGNATION_ACTIVATED_AND_VALIDITY_PERIOD_CHANGED, oldRtfAss, rtfAss, AuthTables.ASS, null);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }
}
