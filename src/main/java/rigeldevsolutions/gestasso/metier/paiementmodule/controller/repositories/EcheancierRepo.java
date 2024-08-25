package rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeancier;

public interface EcheancierRepo extends JpaRepository<Echeancier, Long>
{
}
