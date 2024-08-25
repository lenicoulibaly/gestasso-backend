package rigeldevsolutions.gestasso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;

//@Component
public class CustomEnvironmentPostProcessor{
    @Autowired ConfigurableEnvironment environment;
    //@PostConstruct
    public void postProcessEnvironment() {
        // Supprimer la propriété que vous ne souhaitez pas
        System.out.println("======================HEEEEEE=====================");
        environment.getPropertySources().remove("report.location");
        String location = environment.getProperty("report.location");
        System.out.println("location = ");
    }
}