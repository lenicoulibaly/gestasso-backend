package rigeldevsolutions.gestasso.initer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.PrvRepo;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.PrvToRoleAssRepo;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.RoleRepo;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppRole;
import rigeldevsolutions.gestasso.authmodule.model.entities.PrvToRoleAss;

import java.time.LocalDate;

@Service @RequiredArgsConstructor
public class RoleIniter implements Initer
{
    private final PrvToRoleAssRepo ptrRepo;
    private final PrvRepo prvRepo;
    private final RoleRepo roleRepo;

    @Override
    public void init()
    {
        AppRole roleObs = roleRepo.save(new AppRole( "ROL-OBS", "Observateur global"));
        AppRole roleObsCompta = roleRepo.save(new AppRole( "ROL-OBS-COMPTA", "Observateur comptable"));
        AppRole roleObsTech = roleRepo.save(new AppRole( "ROL-OBS-TECH", "Observateur technique"));
        AppRole roleObsFonc = roleRepo.save(new AppRole( "ROL-OBS-FONC", "Observateur fonctionnel"));
        AppRole roleAdm = roleRepo.save(new AppRole( "ROL-ADM", "Administrateur global"));
        AppRole roleAdmTech = roleRepo.save(new AppRole( "ROL-ADM-TECH", "Administrateur technique"));
        AppRole roleAdmFonc = roleRepo.save(new AppRole( "ROL-ADM-FONC", "Administrateur fonctionnel"));
        AppRole roleCompta = roleRepo.save(new AppRole( "ROL-COMPTA", "Comptable"));
        AppRole roleDev = roleRepo.save(new AppRole( "ROL-DEV", "Developpeur"));

        prvRepo.findAll().forEach(prv->ptrRepo.save(new PrvToRoleAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), prv, roleDev)));
    }
}
