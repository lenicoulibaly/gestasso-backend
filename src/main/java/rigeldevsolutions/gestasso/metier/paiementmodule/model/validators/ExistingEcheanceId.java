package rigeldevsolutions.gestasso.metier.paiementmodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.EcheanceRepo;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ExistingEcheanceId.ExistingAssIdValidator.class})
public @interface ExistingEcheanceId
{
    String message() default "Echeance introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingAssIdValidator implements ConstraintValidator<ExistingEcheanceId, Long>
    {
        private final EcheanceRepo echeancierRepo;
        @Override
        public boolean isValid(Long echeancierId, ConstraintValidatorContext context)
        {
            return echeancierRepo.existsById(echeancierId);
        }
    }
}
