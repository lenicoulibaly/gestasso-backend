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
@Constraint(validatedBy = {UniqueTel.NoneExistingTelValidatorOnCreate.class, UniqueTel.NoneExistingTelValidatorOnUpdateUser.class, UniqueTel.NoneExistingTelValidatorOnUpdateMembre.class})
@Documented
public @interface UniqueTel
{
    String message() default "N° téléphone déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneExistingTelValidatorOnCreate implements ConstraintValidator<UniqueTel, String>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return !userRepo.alreadyExistsByTel(value);
        }
    }

    @Component @RequiredArgsConstructor
    class NoneExistingTelValidatorOnUpdateUser implements ConstraintValidator<UniqueTel, UpdateUserDTO>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(UpdateUserDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getTel() == null) return true;
            if(dto.getUserId() == null) return !userRepo.alreadyExistsByTel(dto.getTel());
            return !userRepo.alreadyExistsByTel(dto.getTel(), dto.getUserId());
        }
    }

    @Component @RequiredArgsConstructor
    class NoneExistingTelValidatorOnUpdateMembre implements ConstraintValidator<UniqueTel, CreateMembreDTO>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(CreateMembreDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getTel() == null) return true;
            if(dto.getUserId() == null) return !userRepo.alreadyExistsByTel(dto.getTel());
            return !userRepo.alreadyExistsByTel(dto.getTel(), dto.getUserId());
        }
    }
}


