package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.UpdateEcheanceDTO;

public interface IEcheanceService
{
    ReadEcheanceDTO createEcheance(CreateEcheanceDTO dto, ActionIdentifier ai);
    ReadEcheanceDTO updateEcheance(UpdateEcheanceDTO dto, ActionIdentifier ai);
}
