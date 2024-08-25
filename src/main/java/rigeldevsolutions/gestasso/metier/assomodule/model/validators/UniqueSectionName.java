package rigeldevsolutions.gestasso.metier.assomodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.AssoRepo;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.SectionRepo;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateSectionDTO;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueSectionName.UniqueSectionNameValidator.class})
@Documented
public @interface UniqueSectionName
{
    String message() default "Nom de section déjà utilisé";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueSectionNameValidator implements ConstraintValidator<UniqueSectionName, CreateSectionDTO>
    {
        private final SectionRepo sectionRepo;

        @Override
        public boolean isValid(CreateSectionDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            return !sectionRepo.existsByNameAndAssoId(dto.getSectionName(), dto.getAssoId()) ;
        }
    }
}