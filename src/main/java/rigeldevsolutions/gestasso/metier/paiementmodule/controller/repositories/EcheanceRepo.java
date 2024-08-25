package rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeance;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeancier;

public interface EcheanceRepo extends JpaRepository<Echeance, Long>
{
}
