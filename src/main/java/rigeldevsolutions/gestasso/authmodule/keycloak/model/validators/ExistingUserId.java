package rigeldevsolutions.gestasso.authmodule.keycloak.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakUserRepo;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingAdhesionId;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingAdhesionId.ExistingAdhesionIdValidator.class})
@Documented
public @interface ExistingUserId
{
    String message() default "L'ID de l'utilisateur est introuvable";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingUserIdValidator implements ConstraintValidator<ExistingUserId, String>
    {
        private final KeycloakUserRepo kuRepo;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return kuRepo.existsById(value) ;
        }
    }
}
