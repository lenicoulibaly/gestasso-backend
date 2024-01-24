package rigeldevsolutions.gestasso.authmodule.controller.services.spec;

import rigeldevsolutions.gestasso.authmodule.model.entities.AccountToken;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;

public interface IAccountTokenService
{
    AccountToken createAccountToken(AppUser appUser);
    AccountToken createAccountToken(Long agentId);
}
