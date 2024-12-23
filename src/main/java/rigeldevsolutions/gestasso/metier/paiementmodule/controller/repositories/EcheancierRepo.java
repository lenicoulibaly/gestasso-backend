package rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheancierDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeancier;

public interface EcheancierRepo extends JpaRepository<Echeancier, Long>
{
    @Query("""
        select new rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheancierDTO
        (e.typeEcheancier.uniqueCode, e.typeEcheancier.name, e.frequence.uniqueCode, e.frequence.name, e.name, e.exercice.exeCode) 
        from Echeancier e where e.echeancierId = ?1""")
    ReadEcheancierDTO getEcheancier(Long echeancierId);

    @Query("select e.echeancierId from Echeancier e where e.frequence.uniqueCode = ?1 and e.exercice.exeCode = ?2")
    Long getEcheancierNatIdByFrequenceAndExeCode(String frequenceTypeCode, int annee);

    @Query("select e from Echeancier e where e.frequence.uniqueCode = ?1 and e.exercice.exeCode = ?2")
    Echeancier findByFrequenceTypeCodeAndExeCode(String frequenceTypeCode, Long exeCode);
}
