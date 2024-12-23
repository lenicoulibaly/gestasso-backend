package rigeldevsolutions.gestasso.authmodule.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateMembreDTO;

@Getter
public class MembreCreatedEvent extends ApplicationEvent
{
    private AppUser user;
    private CreateMembreDTO dto;
    private ActionIdentifier ai;

    public MembreCreatedEvent(Object source, AppUser user, CreateMembreDTO dto, ActionIdentifier ai) {
        super(source);
        this.user = user;
        this.dto = dto;
        this.ai = ai;
    }
}
