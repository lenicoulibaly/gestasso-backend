package rigeldevsolutions.gestasso.grademodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rigeldevsolutions.gestasso.grademodule.model.entities.Grade;
import rigeldevsolutions.gestasso.grademodule.model.enums.Categorie;
import rigeldevsolutions.gestasso.sharedmodule.enums.PersStatus;

import java.util.List;

public interface GradeRepo extends JpaRepository<Grade, String>
{

    @Query("select g from Grade g where g.categorie = ?1")
    List<Grade> findByCategorie(Categorie categorie);

    @Query("select g from Grade g where upper(g.nomGrade) like upper(concat('%', ?1, '%'))")
    Page<Grade> searchPageOfGrades(String nomGrade, Pageable pageable);

    @Query("select (count(g) > 0) from Grade g where g.rang = ?1 and g.categorie = ?2")
    boolean existsByRankAndCategory(int rang, Categorie categorie);

    @Query("select (count(g) > 0) from Grade g where g.gradeCode <> ?1 and g.rang = ?2 and g.categorie = ?3")
    boolean existsByRankAndCategory(String codeGrade, int rang, Categorie categorie);



}