package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheancierDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheancierDTO;

public interface IEcheancierService
{
    //List<LocalDate> getDatesOfEcheancier(String frequenceTypeCode, int annee);
    ReadEcheancierDTO createEcheancier(CreateEcheancierDTO dto);
    ReadEcheancierDTO createEcheancierNaturel(CreateEcheancierDTO dto);
    ReadEcheancierDTO getEcheancier(Long echeancierId);
    ReadEcheancierDTO getEcheancierNaturel(String frequenceTypeCode, int annee);


}
