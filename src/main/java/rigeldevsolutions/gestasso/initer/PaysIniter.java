package rigeldevsolutions.gestasso.initer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.NatRepo;
import rigeldevsolutions.gestasso.authmodule.model.entities.Nationalite;
import rigeldevsolutions.gestasso.grademodule.controller.repositories.GradeRepo;
import rigeldevsolutions.gestasso.grademodule.model.entities.Grade;
import rigeldevsolutions.gestasso.grademodule.model.enums.Categorie;

@Component @RequiredArgsConstructor
public class PaysIniter implements Initer
{
    private final NatRepo natRepo;
    @Override
    public void init() {
        Nationalite civ = natRepo.save(new Nationalite("CIV", "Ivoirienne", "Côte d'Ivoire"));
        Nationalite sen = natRepo.save(new Nationalite("SEN", "Sénégalaise", "Sénégal"));
        Nationalite fr = natRepo.save(new Nationalite("FR", "Française", "France"));

    }
}
