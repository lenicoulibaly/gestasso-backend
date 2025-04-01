package rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Versement;

import java.time.LocalDate;
import java.util.List;

public interface VersementRepo extends JpaRepository<Versement, Long>
{
    @Query("select v.cotisation.association.assoId from Versement v where v.versementId = ?1")
    Long getAssoIdByVersementId(Long versementId);

    @Query("""
    select new rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO(
    v.versementId, v.codeVersement, v.dateVersement, v.montant, v.montantLettre, v.modePaiement.uniqueCode, v.modePaiement.name,
    v.typePaiement.uniqueCode, v.typePaiement.name, a.adhesionId, a.userId, c.cotisationId, c.nomCotisation, c.motif, asso.assoId, asso.assoName, asso.sigle) 
    from Versement v join v.adhesion a join v.cotisation c join c.association asso
    where v.versementId = ?1
    """)
    VersementDTO findVersmentById(Long versementId);

    @Query("""
    select new rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO(
    v.versementId, v.codeVersement, v.dateVersement, v.montant, v.montantLettre, v.modePaiement.uniqueCode, v.modePaiement.name,
    v.typePaiement.uniqueCode, v.typePaiement.name, a.adhesionId, a.userId, c.cotisationId, c.nomCotisation, c.motif, asso.assoId, asso.assoName, asso.sigle) 
    from Versement v join v.adhesion a join v.cotisation c join c.association asso
    where v.cotisation.cotisationId = :cotisationId and v.dateVersement between :from and :to and
    (locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(v.codeVersement, '') ) as string))) >0 or a.userId in :usersIds)
    """)
    Page<VersementDTO> search(@Param("cotisationId") Long cotisationId, @Param("from") LocalDate from, @Param("to")LocalDate to, @Param("key") String key, @Param("usersIds") List<String> usersIds, Pageable pageable);
}