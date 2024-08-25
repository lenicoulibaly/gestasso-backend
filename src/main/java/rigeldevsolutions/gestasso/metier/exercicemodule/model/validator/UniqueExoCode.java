package rigeldevsolutions.gestasso.metier.exercicemodule.model.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.exercicemodule.controller.repositories.ExerciceRepo;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.UpdateExerciceDTO;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueExoCode.UniqueExoCodeValidatorOnCreate.class, UniqueExoCode.UniqueExoCodeValidatorOnUpdate.class})
@Documented
public @interface UniqueExoCode
{
    String message() default "Exercice déjà existant";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueExoCodeValidatorOnCreate implements ConstraintValidator<UniqueExoCode, Long>
    {
        private final ExerciceRepo ExoRepo;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return !ExoRepo.alreadyExistsByCode(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueExoCodeValidatorOnUpdate implements ConstraintValidator<UniqueExoCode, UpdateExerciceDTO>
    {
        private final ExerciceRepo ExoRepo;
        @Override
        public boolean isValid(UpdateExerciceDTO dto, ConstraintValidatorContext context) {
            return !ExoRepo.alreadyExistsByCode(dto.getExeCode());
        }
    }
}
