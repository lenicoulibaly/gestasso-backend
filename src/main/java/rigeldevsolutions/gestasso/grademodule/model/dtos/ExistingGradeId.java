package rigeldevsolutions.gestasso.grademodule.model.dtos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import rigeldevsolutions.gestasso.grademodule.controller.repositories.GradeRepo;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingGradeId.ExistingGradeIdValidator.class})
public @interface ExistingGradeId
{
    String message() default "Ce grade n'existe pas";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingGradeIdValidator implements ConstraintValidator<ExistingGradeId, Long>
    {
        private final GradeRepo gradeRepo;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value==null) return false;
            return gradeRepo.existsById(value);
        }
    }
}
