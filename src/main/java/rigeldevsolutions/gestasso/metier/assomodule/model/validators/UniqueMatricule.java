package rigeldevsolutions.gestasso.metier.assomodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakUserRepo;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.AdhesionDTO;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueMatricule.UniqueMatriculeValidatorOnCreate.class, UniqueMatricule.UniqueMatriculeValidatorOnUpdate.class})
@Documented
public @interface UniqueMatricule
{
    String message() default "Matricule déjà utilisé";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueMatriculeValidatorOnUpdate implements ConstraintValidator<UniqueMatricule, AdhesionDTO>
    {
        private final KeycloakUserRepo keycloakUserRepo;

        @Override
        public boolean isValid(AdhesionDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getMatriculeFonctionnaire() == null || dto.getUserId() == null) return true;
            return !keycloakUserRepo.existsByMatricule(dto.getMatriculeFonctionnaire(), dto.getUserId()) ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueMatriculeValidatorOnCreate implements ConstraintValidator<UniqueMatricule, String>
    {
        private final KeycloakUserRepo keycloakUserRepo;

        @Override
        public boolean isValid(String matricule, ConstraintValidatorContext context)
        {
            return !keycloakUserRepo.existsByMatricule(matricule) ;
        }
    }
}