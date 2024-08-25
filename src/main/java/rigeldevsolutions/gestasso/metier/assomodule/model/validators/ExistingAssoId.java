package rigeldevsolutions.gestasso.metier.assomodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.AssoRepo;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.CreateCotisationDTO;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingAssoId.ExistingAssoIdValidator.class})
@Documented
public @interface ExistingAssoId
{
    String message() default "L'ID de l'association est introuvable";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingAssoIdValidator implements ConstraintValidator<ExistingAssoId, Long>
    {
        private final AssoRepo assoRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return assoRepo.existsById(value) ;
        }
    }
}