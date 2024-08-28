package rigeldevsolutions.gestasso.authmodule.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rigeldevsolutions.gestasso.authmodule.model.entities.Nationalite;

public interface NatRepo extends JpaRepository<Nationalite, String> {
}