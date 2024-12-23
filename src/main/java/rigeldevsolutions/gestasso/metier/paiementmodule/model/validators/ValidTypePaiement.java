package rigeldevsolutions.gestasso.metier.paiementmodule.model.validators;

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
@Constraint(validatedBy = {ValidTypePaiement.ValidTypePaiementValidator.class})
@Documented
public @interface ValidTypePaiement
{
    String message() default "Type de paiement invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidTypePaiementValidator implements ConstraintValidator<ValidTypePaiement, String>
    {
        private final TypeRepo typeRepo;
        @Override
        public boolean isValid(String typePaiement, ConstraintValidatorContext context) {
            if(typePaiement == null) return true;
            return typeRepo.existsByGroupAndUniqueCode(TypeGroup.TYPE_PAIEMENT, typePaiement);
        }
    }
}


