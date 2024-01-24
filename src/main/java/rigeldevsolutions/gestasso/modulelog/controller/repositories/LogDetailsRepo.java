package rigeldevsolutions.gestasso.modulelog.controller.repositories;

import rigeldevsolutions.gestasso.modulelog.model.entities.LogDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogDetailsRepo extends JpaRepository<LogDetails, Long> {
}
