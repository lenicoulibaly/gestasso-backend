package rigeldevsolutions.gestasso.authmodule.controller.services.spec;

import org.springframework.transaction.event.TransactionalEventListener;
import rigeldevsolutions.gestasso.authmodule.model.entities.AccountToken;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;
import rigeldevsolutions.gestasso.authmodule.model.events.AdherantCreatedEvent;

public interface IAccountTokenService
{
    AccountToken createAccountToken(AppUser appUser, ActionIdentifier ai);
    AccountToken createAccountToken(Long agentId, ActionIdentifier ai);

    @TransactionalEventListener
    void onAdherantCreatedEvent(AdherantCreatedEvent event);
}
