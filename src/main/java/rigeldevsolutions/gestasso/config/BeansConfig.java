package rigeldevsolutions.gestasso.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IJwtService;
import rigeldevsolutions.gestasso.modulelog.controller.service.AuditorAwareImpl;

@Configuration
public class BeansConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public LocalValidatorFactoryBean validator(SpringConstraintValidatorFactory constraintValidatorFactory) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setConstraintValidatorFactory(constraintValidatorFactory);
        return factoryBean;
    }

    @Bean
    public AuditorAware<String> auditorProvider(IJwtService jwtService)
    {
        return new AuditorAwareImpl(jwtService);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
