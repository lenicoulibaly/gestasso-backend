package rigeldevsolutions.gestasso.authmodule.model.dtos.validators;

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
@Constraint(validatedBy = {ValidCodeCivilite.ValidCodeCiviliteValidator.class})
@Documented
public @interface ValidCodeCivilite
{
    String message() default "Civilité invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidCodeCiviliteValidator implements ConstraintValidator<ValidCodeCivilite, String>
    {
        private final TypeRepo typeRepo;
        @Override
        public boolean isValid(String codeCivilite, ConstraintValidatorContext context) {
            if(codeCivilite == null) return true;
            return typeRepo.existsByGroupAndUniqueCode(TypeGroup.TYPE_CIVILITE, codeCivilite);
        }
    }
}


