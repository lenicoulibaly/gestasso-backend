package rigeldevsolutions.gestasso.authmodule.controller.services.spec;

import rigeldevsolutions.gestasso.authmodule.model.entities.AccountToken;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;

public interface IAccountTokenService
{
    AccountToken createAccountToken(AppUser appUser, ActionIdentifier ai);
    AccountToken createAccountToken(Long agentId, ActionIdentifier ai);
}
