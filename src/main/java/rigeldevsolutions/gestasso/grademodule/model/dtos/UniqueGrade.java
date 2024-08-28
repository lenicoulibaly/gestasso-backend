package rigeldevsolutions.gestasso.grademodule.model.dtos;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import rigeldevsolutions.gestasso.grademodule.controller.repositories.GradeRepo;
import rigeldevsolutions.gestasso.grademodule.model.enums.Categorie;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueGrade.UniqueGradeValidator.class, UniqueGrade.UniqueGradeValidatorOnUpdate.class})
public @interface UniqueGrade
{
    String message() default "Ce grade existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueGradeValidator implements ConstraintValidator<UniqueGrade, CreateGradeDTO>
    {
        private final GradeRepo gradeRepo;
        @Override
        public boolean isValid(CreateGradeDTO dto, ConstraintValidatorContext context)
        {
            if(dto==null) return false;
            return !gradeRepo.existsByRankAndCategory(dto.getRang(), EnumUtils.getEnum(Categorie.class, dto.getCategorie()));
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueGradeValidatorOnUpdate implements ConstraintValidator<UniqueGrade, UpdateGradeDTO>
    {
        private final GradeRepo gradeRepo;
        @Override
        public boolean isValid(UpdateGradeDTO dto, ConstraintValidatorContext context)
        {
            if(dto==null) return false;
            return !gradeRepo.existsByRankAndCategory(dto.getGradeCode(), dto.getRang(), EnumUtils.getEnum(Categorie.class, dto.getCategorie()));
        }
    }
}
