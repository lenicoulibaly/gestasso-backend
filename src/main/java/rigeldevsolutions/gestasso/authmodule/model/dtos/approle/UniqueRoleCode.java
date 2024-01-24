package rigeldevsolutions.gestasso.authmodule.model.dtos.approle;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.RoleRepo;
import rigeldevsolutions.gestasso.authmodule.model.dtos.asignation.PrvsToRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueRoleCode.UniqueRoleCodeValidatorOnCreate.class})
@Documented
public @interface UniqueRoleCode
{
    String message() default "Code de rôle déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueRoleCodeValidatorOnCreate implements ConstraintValidator<UniqueRoleCode, String>
    {
        private final RoleRepo roleRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !roleRepo.existsByCode(value);
        }
    }
}


