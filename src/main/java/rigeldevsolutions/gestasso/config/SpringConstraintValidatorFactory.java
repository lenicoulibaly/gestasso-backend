package rigeldevsolutions.gestasso.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringConstraintValidatorFactory implements ConstraintValidatorFactory {

    @Autowired
    private final ApplicationContext applicationContext;

    public SpringConstraintValidatorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        return applicationContext.getBean(key);
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> instance) {
        // Rien à faire : Spring gère les beans
    }
}
