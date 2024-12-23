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
@Constraint(validatedBy = {ValidModePrelevement.ValidModePrelevementValidator.class})
@Documented
public @interface ValidModePrelevement
{
    String message() default "Mode de prélèvement invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidModePrelevementValidator implements ConstraintValidator<ValidModePrelevement, String>
    {
        private final TypeRepo typeRepo;
        @Override
        public boolean isValid(String modePrelevement, ConstraintValidatorContext context) {
            if(modePrelevement == null) return true;
            return typeRepo.existsByGroupAndUniqueCode(TypeGroup.MODE_PRELEVEMENT, modePrelevement);
        }
    }
}


