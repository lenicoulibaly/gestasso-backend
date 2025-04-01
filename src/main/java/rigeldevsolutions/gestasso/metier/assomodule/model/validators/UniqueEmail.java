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
@Constraint(validatedBy = {UniqueEmail.UniqueEmailValidatorOnCreate.class, UniqueEmail.UniqueEmailValidatorOnUpdate.class})
@Documented
public @interface UniqueEmail
{
    String message() default "Email déjà utilisé";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueEmailValidatorOnUpdate implements ConstraintValidator<UniqueEmail, AdhesionDTO>
    {
        private final KeycloakUserRepo keycloakUserRepo;

        @Override
        public boolean isValid(AdhesionDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getEmail() == null || dto.getUserId() == null) return true;
            return !keycloakUserRepo.existsByEmail(dto.getEmail(), dto.getUserId()) ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueEmailValidatorOnCreate implements ConstraintValidator<UniqueEmail, String>
    {
        private final KeycloakUserRepo keycloakUserRepo;

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context)
        {
            return !keycloakUserRepo.existsByEmail(email) ;
        }
    }
}