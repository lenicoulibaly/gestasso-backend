package rigeldevsolutions.gestasso.authmodule.model.dtos.appuser;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateMembreDTO;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueEmail.NoneExistingEmailValidatorOnCreate.class, UniqueEmail.NoneExistingEmailValidatorOnUpdate.class})
@Documented
public @interface UniqueEmail
{
    String message() default "Adresse mail déjà attribuée";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneExistingEmailValidatorOnCreate implements ConstraintValidator<UniqueEmail, String>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !userRepo.alreadyExistsByEmail(value);
        }
    }

    @Component @RequiredArgsConstructor
    class NoneExistingEmailValidatorOnUpdate implements ConstraintValidator<UniqueEmail, CreateMembreDTO>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(CreateMembreDTO dto, ConstraintValidatorContext context) {
            if(dto.getEmail() == null) return true;
            if(dto.getUserId() == null) return !userRepo.alreadyExistsByTel(dto.getEmail());
            return !userRepo.alreadyExistsByEmail(dto.getEmail(), dto.getUserId());
        }
    }
}




