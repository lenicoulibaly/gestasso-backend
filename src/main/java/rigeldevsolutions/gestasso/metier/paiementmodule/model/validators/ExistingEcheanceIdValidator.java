package rigeldevsolutions.gestasso.metier.paiementmodule.model.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import rigeldevsolutions.gestasso.config.SpringContext;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.EcheanceRepo;

public class ExistingEcheanceIdValidator implements ConstraintValidator<ExistingEcheanceId, Long>
{
    private EcheanceRepo echeanceRepo;
    public ExistingEcheanceIdValidator() {
        this.echeanceRepo = SpringContext.getBean(EcheanceRepo.class);
    }

    @Override
    public boolean isValid(Long echeanceId, ConstraintValidatorContext context)
    {
        if(echeanceId == null) return true;
        if (echeanceRepo == null) {
            throw new IllegalStateException("Dépôt non injecté");
        }
        return echeanceRepo.existsById(echeanceId);
    }
}
/*
Caused by: java.lang.NoSuchMethodException: rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators.ExistingCotisationIdValidator.<init>()
	at java.base/java.lang.Class.getConstructor0(Class.java:3585)
	at java.base/java.lang.Class.getConstructor(Class.java:2271)
	at org.hibernate.validator.internal.util.privilegedactions.NewInstance.run(NewInstance.java:41)
	... 163 more
 */