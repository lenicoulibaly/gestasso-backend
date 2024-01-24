package rigeldevsolutions.gestasso.grademodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rigeldevsolutions.gestasso.grademodule.model.entities.Grade;
import rigeldevsolutions.gestasso.grademodule.model.enums.Categorie;
import rigeldevsolutions.gestasso.sharedmodule.enums.PersStatus;

import java.util.List;

public interface GradeRepo extends JpaRepository<Grade, Long>
{
    @Query("select g from Grade g where g.status = 'ACTIVE'")
    List<Grade> getActiveGrades();
    @Query("select g.idGrade from Grade g where g.status = 'ACTIVE'")
    List<Long> getActiveGradesIds();

    @Query("select g from Grade g where g.categorie = ?1 and g.status = 'ACTIVE'")
    List<Grade> findByCategorieAndStatus(String categorie);

    @Query("select g from Grade g where upper(g.nomGrade) like upper(concat('%', ?1, '%')) and g.status = 'ACTIVE'")
    Page<Grade> searchPageOfGrades(String nomGrade, Pageable pageable);

    @Query("select count(distinct g) from Grade g where upper(g.nomGrade) like upper(concat('%', ?1, '%')) and g.status = 'ACTIVE'")
    long countByGradeNameKey(String nomGrade);

    @Query("select count(g) from Grade g where g.status = 'ACTIVE'")
    long countActiveGrades();

    @Query("select count(g) from Grade g where g.status = 'DELETED'")
    long countDeletedGrades();





    @Query("select g from Grade g where upper(g.nomGrade) like upper(concat('%', ?1, '%')) and g.status = 'DELETED'")
    Page<Grade> searchPageOfDeletedGrades(String nomGrade, Pageable pageable);

    @Query("select g from Grade g where g.status = ?1")
    List<Grade> findByStatus(PersStatus status);

    @Query("select (count(g) > 0) from Grade g where g.rang = ?1 and g.categorie = ?2")
    boolean existsByRankAndCategory(int rang, Categorie categorie);

    @Query("select (count(g) > 0) from Grade g where g.idGrade <> ?1 and g.rang = ?2 and g.categorie = ?3")
    boolean existsByRankAndCategory(Long idGrade, int rang, Categorie categorie);



}