package rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.CreateCotisationDTO;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AssoIdAndSectionIdNotNullTogether.UniqueSectionNameValidator.class})
@Documented
public @interface AssoIdAndSectionIdNotNullTogether
{
    String message() default "Veuillez selectionner une assocition ou une section d'association";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueSectionNameValidator implements ConstraintValidator<AssoIdAndSectionIdNotNullTogether, CreateCotisationDTO>
    {

        @Override
        public boolean isValid(CreateCotisationDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAssoId() == null && dto.getSectionId() == null) return false;

            return true;
        }
    }
}