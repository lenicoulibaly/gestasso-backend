package rigeldevsolutions.gestasso.initer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.sharedmodule.enums.PersStatus;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeRepo;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;
import rigeldevsolutions.gestasso.typemodule.model.enums.TypeGroup;

//@Component
@RequiredArgsConstructor
public class PartialInitStarter
{
    private final TypeRepo typeRepo;
    @PostConstruct
    void start()
    {
        Type monsieur = typeRepo.save(new Type("M.", TypeGroup.TYPE_CIVILITE, "Monsieur", PersStatus.ACTIVE, null, null));
        Type mme = typeRepo.save(new Type("MME", TypeGroup.TYPE_CIVILITE, "Madame", PersStatus.ACTIVE, null, null));
        Type mlle = typeRepo.save(new Type("MLLE", TypeGroup.TYPE_CIVILITE, "Mademoiselle", PersStatus.ACTIVE, null, null));
    }
}
