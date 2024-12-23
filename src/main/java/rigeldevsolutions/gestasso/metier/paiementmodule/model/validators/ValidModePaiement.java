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
@Constraint(validatedBy = {ValidModePaiement.ValidModePaiementValidator.class})
@Documented
public @interface ValidModePaiement
{
    String message() default "Mode de paiement invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidModePaiementValidator implements ConstraintValidator<ValidModePaiement, String>
    {
        private final TypeRepo typeRepo;
        @Override
        public boolean isValid(String modePaiement, ConstraintValidatorContext context) {
            if(modePaiement == null) return true;
            return typeRepo.existsByGroupAndUniqueCode(TypeGroup.MODE_PAIEMENT, modePaiement);
        }
    }
}


