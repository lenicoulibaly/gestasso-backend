package rigeldevsolutions.gestasso.metier.paiementmodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.PaiementRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.PaiementDTO;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueReference.UniqueReferenceValidator.class})
@Documented
public @interface UniqueReference
{
    String message() default "Référence paiement déjà utilisée";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueReferenceValidator implements ConstraintValidator<UniqueReference, PaiementDTO>
    {
        private final PaiementRepo paiementRepo;
        @Override
        public boolean isValid(PaiementDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getPaiementId() == null) return !paiementRepo.existsByReference(dto.getReference());
            return !paiementRepo.existsByReference(dto.getReference(), dto.getPaiementId());
        }
    }
}


