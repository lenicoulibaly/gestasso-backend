package rigeldevsolutions.gestasso;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IJwtService;
import rigeldevsolutions.gestasso.modulelog.controller.service.AuditorAwareImpl;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class GestassoApplication
{
    @Bean
    public AuditorAware<String> auditorProvider(IJwtService jwtService)
    {
        return new AuditorAwareImpl(jwtService);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
    public static void main(String[] args) {

        SpringApplication.run(GestassoApplication.class, args);
    }
}
