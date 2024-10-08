package rigeldevsolutions.gestasso.authmodule.model.dtos.appprivilege;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.PrvRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingPrivilegeCode.ExistingPrivilegeIdValidator.class})
@Documented
public @interface ExistingPrivilegeCode
{
    String message() default "Invalid privilegeId";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingPrivilegeIdValidator implements ConstraintValidator<ExistingPrivilegeCode, String>
    {
        private final PrvRepo prvRepo;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return prvRepo.existsById(value);
        }
    }
}
