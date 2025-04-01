package rigeldevsolutions.gestasso.metier.assomodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.AdhesionRepo;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingAdhesionId.ExistingAdhesionIdValidator.class})
@Documented
public @interface ExistingAdhesionId
{
    String message() default "L'ID du membre est introuvable";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingAdhesionIdValidator implements ConstraintValidator<ExistingAdhesionId, Long>
    {
        private final AdhesionRepo adhesionRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return adhesionRepo.existsById(value) ;
        }
    }
}