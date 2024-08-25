package rigeldevsolutions.gestasso.metier.assomodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.AssoRepo;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.UpdateAssociationDTO;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueAssoName.UniqueAssoNameValidatorOnCreate.class, UniqueAssoName.UniqueAssoNameValidatorOnUpdate.class})
@Documented
public @interface UniqueAssoName
{
    String message() default "Nom d'association déjà utilisé";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueAssoNameValidatorOnUpdate implements ConstraintValidator<UniqueAssoName, UpdateAssociationDTO>
    {
        private final AssoRepo assoRepo;

        @Override
        public boolean isValid(UpdateAssociationDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAssoName() == null || dto.getAssoId() == null) return true;
            return !assoRepo.existsByName(dto.getAssoName(), dto.getAssoId()) ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueAssoNameValidatorOnCreate implements ConstraintValidator<UniqueAssoName, String>
    {
        private final AssoRepo assoRepo;

        @Override
        public boolean isValid(String name, ConstraintValidatorContext context)
        {
            return !assoRepo.existsByName(name) ;
        }
    }
}