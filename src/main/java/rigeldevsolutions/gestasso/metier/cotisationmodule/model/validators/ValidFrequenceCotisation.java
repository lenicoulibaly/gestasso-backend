package rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeRepo;
import rigeldevsolutions.gestasso.typemodule.model.enums.TypeGroup;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidFrequenceCotisation.ValidFrequenceCotisationValidator.class})
@Documented
public @interface ValidFrequenceCotisation
{
    String message() default "Fréquence invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidFrequenceCotisationValidator implements ConstraintValidator<ValidFrequenceCotisation, String>
    {
        private final TypeRepo typeRepo;
        @Override
        public boolean isValid(String frequenceCode, ConstraintValidatorContext context) {
            if(frequenceCode == null) return true;
            return typeRepo.existsByGroupAndUniqueCode(TypeGroup.TYPE_FREQUENCE, frequenceCode);
        }
    }
}


