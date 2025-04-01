package rigeldevsolutions.gestasso.metier.assomodule.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;

import java.util.List;

@Getter
public class AssociationCreatedEvent extends ApplicationEvent
{
    private Association association;
    private List<CreateSectionDTO> createSectionDTOS;
    public AssociationCreatedEvent(Object source, Association association, List<CreateSectionDTO> createSectionDTOS) {
        super(source);
        this.association = association;
        this.createSectionDTOS = createSectionDTOS;
    }
}
