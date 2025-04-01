package rigeldevsolutions.gestasso.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.services.IJwtService;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.env.KeycloakEnv;

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

    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

    @Bean(name = "keycloak")
    public WebClient keycloakWebClient(IJwtService jwtService, KeycloakEnv env)
    {
        return WebClient.builder()
                .baseUrl(env.keycloakServerAddress)
                .filter(addAuthHeader(jwtService))
                .defaultHeader("Authorization", "Bearer " + jwtService.getCurrentAccessToken())
                .build();
    }
    private ExchangeFilterFunction addAuthHeader(IJwtService jwtService) {
        return (request, next) -> {
            String token = jwtService.getCurrentAccessToken();
            return next.exchange(
                    ClientRequest.from(request)
                            .header("Authorization", "Bearer " + token)
                            .build()
            );
        };
    }


    @Bean
    public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        return NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }

}
