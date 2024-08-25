package rigeldevsolutions.gestasso.metier.assomodule.controller.listeners;

import rigeldevsolutions.gestasso.metier.assomodule.model.events.AssociationCreatedEvent;

public interface IAssociationEventsListener
{
    void onAssociationCreatedEvent(AssociationCreatedEvent event);
}
