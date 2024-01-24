package rigeldevsolutions.gestasso.grademodule.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rigeldevsolutions.gestasso.grademodule.model.entities.Category;

public interface CategoryDAO extends JpaRepository<Category, Long> {
}