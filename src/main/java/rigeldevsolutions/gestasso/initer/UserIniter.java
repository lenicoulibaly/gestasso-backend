package rigeldevsolutions.gestasso.initer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.FunctionRepo;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.UserRepo;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppFunction;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;

import java.time.LocalDate;

@Component @RequiredArgsConstructor
public class UserIniter implements Initer
{
    private final UserRepo userRepo;
    private final PasswordEncoder pe;
    private final FunctionRepo functionRepo;
    @Override
    public void init() {
        AppUser lenigauss = userRepo.save(new AppUser("Leni", "Gauss", pe.encode("1234"), "lenigauss@gmail.com", "0505471049", true, true));

        AppFunction fncDev = functionRepo.save(new AppFunction( "Développer", lenigauss, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        lenigauss.setCurrentFunctionId(fncDev.getId());
        userRepo.save(lenigauss);
    }
}
