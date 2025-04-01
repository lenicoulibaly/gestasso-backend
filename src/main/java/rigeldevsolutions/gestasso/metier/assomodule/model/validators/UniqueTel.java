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
@Constraint(validatedBy = {UniqueTel.UniqueTelValidatorOnCreate.class, UniqueTel.UniqueTelValidatorOnUpdate.class})
@Documented
public @interface UniqueTel
{
    String message() default "Téléphone déjà utilisé";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueTelValidatorOnUpdate implements ConstraintValidator<UniqueTel, AdhesionDTO>
    {
        private final KeycloakUserRepo keycloakUserRepo;

        @Override
        public boolean isValid(AdhesionDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getTel() == null || dto.getUserId() == null) return true;
            return !keycloakUserRepo.existsByTel(dto.getTel(), dto.getUserId()) ;
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueTelValidatorOnCreate implements ConstraintValidator<UniqueTel, String>
    {
        private final KeycloakUserRepo keycloakUserRepo;

        @Override
        public boolean isValid(String tel, ConstraintValidatorContext context)
        {
            return !keycloakUserRepo.existsByTel(tel) ;
        }
    }
}