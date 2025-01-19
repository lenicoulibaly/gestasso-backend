package rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingCotisationIdValidator.class})
@Documented
public @interface ExistingCotisationId
{
    String message() default "L'ID de la cotisation est introuvable";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
}