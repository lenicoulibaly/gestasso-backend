package rigeldevsolutions.gestasso.metier.assomodule.controller.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.assomodule.controller.services.ISectionService;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.events.AssociationCreatedEvent;

import java.util.List;
import java.util.Objects;

@Service @RequiredArgsConstructor
public class AssociationEventsListener implements IAssociationEventsListener
{
    private final ISectionService sectionService;
    @Override @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onAssociationCreatedEvent(AssociationCreatedEvent event)
    {
        List<CreateSectionDTO> createSectionDTOS = event.getCreateSectionDTOS();
        Association association = event.getAssociation();
        ActionIdentifier ai = event.getAi();
        if(createSectionDTOS == null || createSectionDTOS.isEmpty())
        {
            sectionService.createSectionDeBase(association, ai);
        }
        else
        {
            createSectionDTOS.stream()
                    .filter(Objects::nonNull)
                    .forEach(createSectionDTO->
            {
                createSectionDTO.setAssoId(association.getAssoId());
                sectionService.createSection(createSectionDTO, ai);
            });
        }
    }
}
