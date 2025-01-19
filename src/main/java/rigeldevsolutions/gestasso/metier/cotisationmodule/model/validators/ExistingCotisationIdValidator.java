package rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import rigeldevsolutions.gestasso.config.SpringContext;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;


public class ExistingCotisationIdValidator implements ConstraintValidator<ExistingCotisationId, Long>
{
    private CotisationRepo cotisationRepo;
    public ExistingCotisationIdValidator()
    {
        this.cotisationRepo = SpringContext.getBean(CotisationRepo.class);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context)
    {
        if (value == null) return true;
        return cotisationRepo.existsById(value);
    }
}
