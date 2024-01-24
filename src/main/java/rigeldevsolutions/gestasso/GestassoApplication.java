package rigeldevsolutions.gestasso;

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
    public AuditorAware<String> auditorProvider(IJwtService jwtService) {
        return new AuditorAwareImpl(jwtService);
    }

    public static void main(String[] args) {
        SpringApplication.run(GestassoApplication.class, args);
    }

}
