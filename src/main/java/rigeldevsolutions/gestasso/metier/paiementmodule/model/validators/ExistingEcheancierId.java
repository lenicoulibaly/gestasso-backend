package rigeldevsolutions.gestasso.metier.paiementmodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.FunctionRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.EcheancierRepo;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ExistingEcheancierId.ExistingAssIdValidator.class})
public @interface ExistingEcheancierId
{
    String message() default "Echeancier introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingAssIdValidator implements ConstraintValidator<ExistingEcheancierId, Long>
    {
        private final EcheancierRepo echeancierRepo;
        @Override
        public boolean isValid(Long echeancierId, ConstraintValidatorContext context)
        {
            return echeancierRepo.existsById(echeancierId);
        }
    }
}
