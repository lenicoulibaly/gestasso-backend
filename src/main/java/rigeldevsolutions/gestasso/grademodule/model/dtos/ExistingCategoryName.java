package rigeldevsolutions.gestasso.grademodule.model.dtos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import rigeldevsolutions.gestasso.grademodule.model.enums.Categorie;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingCategoryName.ExistingCategoryNameValidator.class})
public @interface ExistingCategoryName
{
    String message() default "Ce nom de catégorie n'existe pas";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCategoryNameValidator implements ConstraintValidator<ExistingCategoryName, String>
    {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value==null) return false;
            return Categorie.exists(value);
        }
    }
}
