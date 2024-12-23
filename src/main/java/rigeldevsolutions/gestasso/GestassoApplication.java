package rigeldevsolutions.gestasso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IJwtService;
import rigeldevsolutions.gestasso.modulelog.controller.service.AuditorAwareImpl;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
    public DecimalFormat decimalFormat()
    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' '); // Set white space as grouping separator
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);

        return decimalFormat;
    }
    public static void main(String[] args) {

        SpringApplication.run(GestassoApplication.class, args);
    }
}
