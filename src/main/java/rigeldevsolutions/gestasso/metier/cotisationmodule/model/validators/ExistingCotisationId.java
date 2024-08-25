package rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingCotisationId.ExistingCotisationIdValidator.class})
@Documented
public @interface ExistingCotisationId
{
    String message() default "L'ID de la cotisation est introuvable";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCotisationIdValidator implements ConstraintValidator<ExistingCotisationId, Long>
    {
        private final CotisationRepo cotisationRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return cotisationRepo.existsById(value) ;
        }
    }
}