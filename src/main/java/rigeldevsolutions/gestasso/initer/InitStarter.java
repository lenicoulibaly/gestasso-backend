package rigeldevsolutions.gestasso.initer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

//@Component
@RequiredArgsConstructor
public class InitStarter
{
    private final TypeIniter typeIniter;
    private final GradeIniter gradeIniter;
    private final PaysIniter paysIniter;
    @PostConstruct
    void start()
    {
        typeIniter.init();
        gradeIniter.init();
        paysIniter.init();
    }
}
