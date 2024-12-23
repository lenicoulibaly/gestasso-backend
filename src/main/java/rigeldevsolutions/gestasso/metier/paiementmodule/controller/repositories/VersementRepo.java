package rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Versement;

public interface VersementRepo extends JpaRepository<Versement, Long> {
}