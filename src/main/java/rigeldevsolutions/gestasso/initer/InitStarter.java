package rigeldevsolutions.gestasso.initer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class InitStarter
{
    private final UserIniter userIniter;
    private final RoleIniter roleIniter;
    private final TypeIniter typeIniter;
    @PostConstruct
    void start()
    {
        typeIniter.init();
        userIniter.init();
        roleIniter.init();
    }
}
