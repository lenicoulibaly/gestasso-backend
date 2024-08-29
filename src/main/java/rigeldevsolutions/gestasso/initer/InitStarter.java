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
    private final GradeIniter gradeIniter;
    private final PaysIniter paysIniter;
    @PostConstruct
    void start()
    {
        typeIniter.init();
        userIniter.init();
        roleIniter.init();
        gradeIniter.init();
        paysIniter.init();
    }
}
