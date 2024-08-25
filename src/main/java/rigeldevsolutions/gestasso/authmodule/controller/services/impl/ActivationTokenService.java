package rigeldevsolutions.gestasso.authmodule.controller.services.impl;

import org.springframework.beans.BeanUtils;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.AccountTokenRepo;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IAccountTokenService;
import rigeldevsolutions.gestasso.authmodule.model.entities.AccountToken;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ActivationTokenService implements IAccountTokenService
{
    private final AccountTokenRepo tokenDao;
    @Override
    public AccountToken createAccountToken(AppUser appUser, ActionIdentifier ai)
    {
        AccountToken token = AccountToken.builder()
                                                   .token(UUID.randomUUID().toString())
                                                   .alreadyUsed(false)
                                                   .creationDate(LocalDateTime.now())
                                                   .expirationDate(LocalDateTime.now().plusDays(1))
                                                   .user(appUser)
                                                   .password(generateInt(1000, 9999))
                                                   .build();
        BeanUtils.copyProperties(ai, token);
        return tokenDao.save(token);
    }

    @Override
    public AccountToken createAccountToken(Long agentId, ActionIdentifier ai)
    {
        AccountToken token =  createAccountToken((AppUser) null, ai);
        token.setAgentId(agentId);
        return token;
    }

    private String generateInt(int borneInf, int borneSup){
        Random random = new Random();
        int nb;
        nb = borneInf+random.nextInt(borneSup-borneInf);
        return String.valueOf(nb);
    }
}