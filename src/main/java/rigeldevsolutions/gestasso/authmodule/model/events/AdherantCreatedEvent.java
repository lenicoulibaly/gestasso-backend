package rigeldevsolutions.gestasso.authmodule.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appuser.CreateAdherantDTO;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;

@Getter
public class AdherantCreatedEvent extends ApplicationEvent
{
    private AppUser user;
    private CreateAdherantDTO dto;
    private ActionIdentifier ai;

    public AdherantCreatedEvent(Object source, AppUser user, CreateAdherantDTO dto, ActionIdentifier ai) {
        super(source);
        this.user = user;
        this.dto = dto;
        this.ai = ai;
    }
}
