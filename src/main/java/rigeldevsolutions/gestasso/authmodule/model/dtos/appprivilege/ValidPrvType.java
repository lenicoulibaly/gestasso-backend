package rigeldevsolutions.gestasso.authmodule.model.dtos.appprivilege;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeRepo;
import rigeldevsolutions.gestasso.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidPrvType.ValidPrvTypeValidator.class})
@Documented
public @interface ValidPrvType
{
    String message() default "Type de privilège invalide";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidPrvTypeValidator implements ConstraintValidator<ValidPrvType, String>
    {
        private final TypeRepo typeRepo;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return typeRepo.typeGroupHasChild(TypeGroup.TYPE_PRV, value) ;
        }
    }
}
