package rigeldevsolutions.gestasso.metier.paiementmodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.UpdateEcheanceDTO;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {CoherentEcheanceDates.CoherentDatesValidatorOnCreate.class,
        CoherentEcheanceDates.CoherentDatesValidatorOnUpdate.class})
public @interface CoherentEcheanceDates
{
    String message() default "La date échéance ne peut être ultérieure à la date buttoire";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class CoherentDatesValidatorOnCreate implements ConstraintValidator<CoherentEcheanceDates, CreateEcheanceDTO>
    {
        @Override
        public boolean isValid(CreateEcheanceDTO dto, ConstraintValidatorContext context)
        {
            return dto.getDateEcheance() == null || dto.getDateButtoire() == null ? true : dto.getDateEcheance().isBefore(dto.getDateButtoire()) || dto.getDateEcheance().isEqual(dto.getDateButtoire());
        }
    }

    @Component @RequiredArgsConstructor
    class CoherentDatesValidatorOnUpdate implements ConstraintValidator<CoherentEcheanceDates, UpdateEcheanceDTO>
    {
        @Override
        public boolean isValid(UpdateEcheanceDTO dto, ConstraintValidatorContext context)
        {
            return dto.getDateEcheance() == null || dto.getDateButtoire() == null ? true : dto.getDateEcheance().isBefore(dto.getDateButtoire()) || dto.getDateEcheance().isEqual(dto.getDateButtoire());
        }
    }
}
