package rigeldevsolutions.gestasso.authmodule.controller.services.spec;

import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.authmodule.model.entities.HistoDetails;

public interface IActionIdentifierService
{
    ActionIdentifier getActionIdentifierFromSecurityContext(String actionName);
}
