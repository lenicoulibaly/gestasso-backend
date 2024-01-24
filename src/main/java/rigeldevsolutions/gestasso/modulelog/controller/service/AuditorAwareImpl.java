package rigeldevsolutions.gestasso.modulelog.controller.service;

import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor @Component
public class AuditorAwareImpl implements AuditorAware<String>
{
    private final IJwtService jwtService;
    @Override
    public Optional<String> getCurrentAuditor() {
        String extractedAuditor = jwtService.extractUsername();
        return Optional.of(extractedAuditor);
    }

}