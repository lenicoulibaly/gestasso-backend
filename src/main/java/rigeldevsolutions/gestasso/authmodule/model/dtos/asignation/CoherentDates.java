package rigeldevsolutions.gestasso.authmodule.model.dtos.asignation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction.UpdateFncDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.CreateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.UpdateCotisationDTO;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {CoherentDates.CoherentDatesValidatorOnCreate.class,
        CoherentDates.CoherentDatesValidatorOnUpdate.class
        , CoherentDates.CoherentDatesValidatorOnCreateCotisation.class,
        CoherentDates.CoherentDatesValidatorOnUpdateCotisation.class})
public @interface CoherentDates
{
    String message() default "La date de début ne peut être ultérieure à la date de fin";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class CoherentDatesValidatorOnCreate implements ConstraintValidator<CoherentDates, CreateFunctionDTO>
    {
        @Override
        public boolean isValid(CreateFunctionDTO dto, ConstraintValidatorContext context)
        {
            return dto.getStartsAt() == null || dto.getEndsAt() == null ? true : dto.getStartsAt().isBefore(dto.getEndsAt()) || dto.getStartsAt().isEqual(dto.getEndsAt());
        }
    }

    @Component @RequiredArgsConstructor
    class CoherentDatesValidatorOnUpdate implements ConstraintValidator<CoherentDates, UpdateFncDTO>
    {
        @Override
        public boolean isValid(UpdateFncDTO dto, ConstraintValidatorContext context)
        {
            return dto.getStartsAt() == null || dto.getEndsAt() == null ? true : dto.getStartsAt().isBefore(dto.getEndsAt()) || dto.getStartsAt().isEqual(dto.getEndsAt());
        }
    }

    @Component @RequiredArgsConstructor
    class CoherentDatesValidatorOnCreateCotisation implements ConstraintValidator<CoherentDates, CreateCotisationDTO>
    {
        @Override
        public boolean isValid(CreateCotisationDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getDateDebutCotisation() == null) return true;
            if(dto.getDateFinCotisation() == null) return true;
            return dto.getDateDebutCotisation().isBefore(dto.getDateFinCotisation()) || dto.getDateDebutCotisation().isEqual(dto.getDateFinCotisation());
        }
    }

    @Component @RequiredArgsConstructor
    class CoherentDatesValidatorOnUpdateCotisation implements ConstraintValidator<CoherentDates, UpdateCotisationDTO>
    {
        @Override
        public boolean isValid(UpdateCotisationDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getDateDebutCotisation() == null) return true;
            if(dto.getDateFinCotisation() == null) return true;
            return dto.getDateDebutCotisation().isBefore(dto.getDateFinCotisation()) || dto.getDateDebutCotisation().isEqual(dto.getDateFinCotisation());
        }
    }
}
