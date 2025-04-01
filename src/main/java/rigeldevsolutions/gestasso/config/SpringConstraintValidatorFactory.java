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
        try {
            // Vérifie si Spring gère ce bean
            return applicationContext.getBean(key);
        } catch (Exception e) {
            // Si ce n'est pas un bean Spring, instancie-le via son constructeur par défaut
            try {
                return key.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("Impossible d'instancier le validateur : " + key, ex);
            }
        }
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> instance) {
        // Rien à faire : Spring gère les beans
    }
}
