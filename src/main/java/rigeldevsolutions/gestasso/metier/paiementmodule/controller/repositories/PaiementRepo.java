package rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Paiement;

import java.math.BigDecimal;
import java.util.List;

public interface PaiementRepo extends JpaRepository<Paiement, Long>
{
    @Query("select (count(p) > 0) from Paiement p where upper(p.reference) = upper(?1) and (?2 is null or p.paiementId <> ?2)")
    boolean existsByReference(String reference, Long paiementId);

    @Query("select (count(p) > 0) from Paiement p where upper(p.reference) = upper(?1)")
    boolean existsByReference(String reference);

    @Query("""
            select new rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO 
            (ec.echeanceId, ec.dateEcheance, ec.nomEcheance,  ec.echeancierId, ec.echeanceEchu)
            from EcheanceCotisation ec 
            where ec.cotisationId = ?1 
                and ec.echeanceId not in (select pc.echeanceId 
                                      from PaiementCotisation pc 
                                      where pc.cotisationId = ?1 
                                            and pc.adhesionId = ?2 
                                            and pc.echeanceSolde)
            order by ec.dateEcheance asc
            """)
    Page<ReadEcheanceDTO> getNextEcheances(Long cotisationId, Long adhesionIs, Pageable pageable);

    @Query("select pc.paiementEcheance from PaiementCotisation pc where pc.cotisationId = ?1 and pc.adhesionId = ?2 and pc.echeanceId = ?3")
    BigDecimal calculateDejaPaye(Long cotisationId, Long adhesionId, Long echeanceId);

    @Query("select sum(pc.paiementEcheance) from PaiementCotisation pc where pc.cotisationId = ?1 and pc.adhesionId = ?2 ")
    BigDecimal calculateDejaPaye(Long cotisationId, Long adhesionId);

    @Query("select e.nomEcheance from Paiement p join p.echeance e where p.versement.versementId = ?1 and p.active order by e.dateEcheance asc")
    List<String> getEcheancesVersement(Long versementId);
}