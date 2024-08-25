package rigeldevsolutions.gestasso.metier.exercicemodule.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.ReadExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.entities.Exercice;

import java.util.List;

public interface ExerciceRepo extends JpaRepository<Exercice, Long> {
    @Query("select (count(e) > 0) from Exercice e where e.exeCode = ?1")
    boolean alreadyExistsByCode(Long exeCode);

    @Query("""
        select new rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.ReadExerciceDTO(e.exeCode,e.exeLibelle,e.exeCourant) 
        from Exercice  e where locate(upper(coalesce(?1, '') ), cast(e.exeCode as string)) >0 
                          or locate(upper(coalesce(?1, '') ), upper(cast(function('unaccent',  coalesce(e.exeLibelle, '') ) as string)) ) >0 
                          order by e.exeCode desc
""")
    List<ReadExerciceDTO> searchExercice(String key);

    @Query("select e from Exercice e where e.exeCourant = true")
    List<Exercice> getExeCourant();

    @Query("select e from Exercice e where e.exeCode = (select max(e1.exeCode) from Exercice e1)")
    Exercice getLastExo();

    @Query("update Exercice e set e.exeCourant =false  where e.exeCourant = true")
    @Modifying
    void setExerciceAsNoneCourant();
}
