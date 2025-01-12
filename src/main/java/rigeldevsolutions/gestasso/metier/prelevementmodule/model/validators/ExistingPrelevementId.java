package rigeldevsolutions.gestasso.metier.prelevementmodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.prelevementmodule.controller.repositories.PrelevementRepo;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ExistingPrelevementId.ExistingPrelevementIdValidator.class})
public @interface ExistingPrelevementId
{
    String message() default "Prélèvement introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingPrelevementIdValidator implements ConstraintValidator<ExistingPrelevementId, Long>
    {
        private final PrelevementRepo prelevementRepo;
        @Override
        public boolean isValid(Long echeancierId, ConstraintValidatorContext context)
        {
            return prelevementRepo.existsById(echeancierId);
        }
    }
}
