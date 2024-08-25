package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheancierDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEchancierDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.enums.Frequence;

import java.time.LocalDate;
import java.util.List;

public interface IEcheancierService
{
    List<LocalDate> getDatesOfEcheancier(Frequence f, int annee);
    ReadEchancierDTO createEcheancier(CreateEcheancierDTO dto, ActionIdentifier ai);
    ReadEchancierDTO createEcheancierNaturel(CreateEcheancierDTO dto, ActionIdentifier ai);
}
