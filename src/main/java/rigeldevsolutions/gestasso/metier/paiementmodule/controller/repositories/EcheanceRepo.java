package rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO2;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeance;

import java.util.List;

public interface EcheanceRepo extends JpaRepository<Echeance, Long>
{
    @Query("""
        select new rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO2
        (e.echeanceId, e.dateEcheance, e.dateButtoire, e.montantEcheance, e.tauxEcheance, e.echeancier.echeancierId)
        from Echeance e where e.echeancier.echeancierId = ?1
""")
    List<ReadEcheanceDTO2> findByEcheancierId(Long echeancierId);

    @Query("select e.echeancier.frequence.uniqueCode from Echeance e where e.echeanceId = ?1")
    String getEcheancierTypeFrequence(Long echeanceId);

    @Query("""
     select new rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO 
            (ech1.echeanceId, ech1.dateEcheance, ech1.nomEcheance,  ech1.echeancier.echeancierId)
    from Echeance ech1 where 
        ech1.echeancier.echeancierId = (select ech2.echeancier.echeancierId from Echeance ech2 where ech2.echeanceId = ?1 and ech1.dateEcheance = (select min(ech3.dateEcheance) from Echeance ech3 where ech3.echeancier.echeancierId = ech2.echeancier.echeancierId and ech3.dateEcheance > ech2.dateEcheance))
       
    """)
    ReadEcheanceDTO getNextEcheance(Long echeanceId);

    @Query("""
    select new rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO 
            (ech1.echeanceId, ech1.dateEcheance, ech1.nomEcheance,  ech1.echeancier.echeancierId)
    from Echeance ech1 where ech1.echeancier.echeancierId = ?1 
    and ech1.dateEcheance = (select max(ech2.dateEcheance) from Echeance ech2 where ech2.echeancier.echeancierId = ?1)
""")
    ReadEcheanceDTO getLastEcheance(Long echeancierId);

    @Query("""
    select new rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO 
            (ech1.echeanceId, ech1.dateEcheance, ech1.nomEcheance,  ech1.echeancier.echeancierId)
    from Echeance ech1 where ech1.echeancier.frequence.uniqueCode = ?1 and ech1.echeancier.typeEcheancier.uniqueCode = 'ECH-NAT'
    and ech1.dateEcheance = (select max(ech2.dateEcheance) from Echeance ech2 where ech2.echeancier.frequence.uniqueCode = ?1 and ech2.echeancier.typeEcheancier.uniqueCode = 'ECH-NAT')
""")
    ReadEcheanceDTO getLastEcheance(String typeFrequence);

/*
    @Query("""
        select e 
        from Echeance e 
        where e.echeancier.frequence.uniqueCode = ?3 
            and e.dateEcheance between ?4 
            and coalesce(?5, e.dateEcheance) 
        order by e.dateEcheance asc 
""")
    Page<ReadEcheanceDTO> findNextEcheances(Long adhesionId, Long cotisationId, String frequenceUniqueCode, LocalDate debutCotisation, LocalDate finCotisation, Pageable pageable);
*/
}
