package rigeldevsolutions.gestasso.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.services.IJwtService;

import java.util.Optional;

@RequiredArgsConstructor @Component
public class AuditorAwareImpl implements AuditorAware<String>
{
    private final IJwtService jwtService;
    @Override
    public Optional<String> getCurrentAuditor()
    {
        String extractedAuditor = jwtService != null ? jwtService.getConnectedUserId() : "UNKNOWN";
        return Optional.of(extractedAuditor==null || extractedAuditor.equals("")? "UNKNOWN": extractedAuditor );
    }
}