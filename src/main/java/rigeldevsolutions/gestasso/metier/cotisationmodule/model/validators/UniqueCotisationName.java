package rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.SectionRepo;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateSectionDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.CreateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.UpdateCotisationDTO;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueCotisationName.UniqueCotisationNameNameValidator.class})
@Documented
public @interface UniqueCotisationName
{
    String message() default "Une cotisation porte déjà ce nom";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueCotisationNameNameValidator implements ConstraintValidator<UniqueCotisationName, CreateCotisationDTO>
    {
        private final CotisationRepo cotisationRepo;

        @Override
        public boolean isValid(CreateCotisationDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAssoId() == null && dto.getSectionId() == null) return true;
            if(dto.getSectionId() != null) return !cotisationRepo.existsByNameAndSectionId(dto.getNomCotisation(), dto.getSectionId()) ;
            return !cotisationRepo.existsByNameAndAssoId(dto.getNomCotisation(), dto.getAssoId()) ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueCotisationNameNameValidatorOnUpdate implements ConstraintValidator<UniqueCotisationName, UpdateCotisationDTO>
    {
        private final CotisationRepo cotisationRepo;

        @Override
        public boolean isValid(UpdateCotisationDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getCotisationId() == null ) return true;

            return !cotisationRepo.existsByNameAndCotisationId(dto.getNomCotisation(), dto.getCotisationId()) ;
        }
    }
}