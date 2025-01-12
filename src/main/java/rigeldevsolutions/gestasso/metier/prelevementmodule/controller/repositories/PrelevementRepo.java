package rigeldevsolutions.gestasso.metier.prelevementmodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeance;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.PrelevementDTO;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.entities.Prelevement;

import java.math.BigDecimal;
import java.util.Optional;

public interface PrelevementRepo extends JpaRepository<Prelevement, Long>
{

    @Query("""
    select new rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.PrelevementDTO
    (
    p.prelevementId, p.nbrAdherant, p.montant, p.montantLettre, p.active
    , e.echeanceId, e.nomEcheance, c.cotisationId, c.nomCotisation, c.motif
    )
    from Prelevement p join p.echeance e join p.cotisation c
    where p.prelevementId = ?1
""")
    PrelevementDTO findPrelevementDtoById(Long prelevementId);

    @Query("select prel.cotisation.montantCotisation from Prelevement prel where prel.prelevementId = ?1")
    Optional<BigDecimal> getMontantCotisationByPrelevementId(Long prelevementId);

    @Query("""
    select new rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.PrelevementDTO(
    cot.montantCotisation, cot.montantCotisationLettre, cot.cotisationId, cot.nomCotisation, cot.motif)
    from Cotisation cot where cot.cotisationId = ?1
""")
    PrelevementDTO getPrelevementEditDto(Long cotisationId);

    @Query("select p.echeance from Prelevement p where p.cotisation.cotisationId = ?1 and p.echeance.dateEcheance = (select max(p2.echeance.dateEcheance) from Prelevement p2 where p2.cotisation.cotisationId = ?1)")
    Echeance getLastEcheancePreleve(Long cotisationId);

    @Query("""
        select new rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.PrelevementDTO(
        p.prelevementId, p.nbrAdherant, p.montant, p.montantLettre, p.active, ech.echeanceId, ech.nomEcheance
        , cot.cotisationId, cot.nomCotisation, cot.motif)
        from Prelevement p 
            join p.echeance ech
            join p.cotisation cot
        where (
            locate(upper(coalesce(:key, '') ), upper(cast(p.nbrAdherant as string))) =1
        or locate(upper(coalesce(:key, '') ), upper(cast(p.montant as string))) =1
        or locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(p.montant, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(ech.nomEcheance, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(cot.nomCotisation, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(cot.motif, '') ) as string))) >0
        ) and cot.cotisationId = :cotisationId
""")
    Page<PrelevementDTO> searchPrelevements(@Param("cotisationId") Long cotisationId, @Param("key")String key, Pageable pageable);
}
