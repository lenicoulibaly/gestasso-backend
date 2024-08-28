package rigeldevsolutions.gestasso.authmodule.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rigeldevsolutions.gestasso.authmodule.model.entities.AccountToken;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;

@Getter
public class AccountActivationTokenCreatedEvent extends ApplicationEvent
{
    private AccountToken accountToken;
    private AppUser user;
    private ActionIdentifier ai;

    public AccountActivationTokenCreatedEvent(Object source, AccountToken token, AppUser user, ActionIdentifier ai) {
        super(source);
        this.accountToken = token;
        this.user = user;
        this.ai = ai;
    }
}