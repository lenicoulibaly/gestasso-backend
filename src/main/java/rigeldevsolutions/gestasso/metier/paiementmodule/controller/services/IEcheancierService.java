package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheancierDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheancierDTO;

public interface IEcheancierService
{
    //List<LocalDate> getDatesOfEcheancier(String frequenceTypeCode, int annee);
    ReadEcheancierDTO createEcheancier(CreateEcheancierDTO dto, ActionIdentifier ai);
    ReadEcheancierDTO createEcheancierNaturel(CreateEcheancierDTO dto, ActionIdentifier ai);
    ReadEcheancierDTO getEcheancier(Long echeancierId);
    ReadEcheancierDTO getEcheancierNaturel(String frequenceTypeCode, int annee);


}
