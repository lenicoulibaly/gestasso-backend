package rigeldevsolutions.gestasso.metier.prelevementmodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.DefautPrelevementDTO;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.entities.DefautPrelevement;

public interface DefautPrelevementRepo extends JpaRepository<DefautPrelevement, Long>
{
    @Query("""
    select new rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.DefautPrelevementDTO(
    dp.defautPrelevementId, dp.montant, dp.montantLettre, dp.motif, dp.active, prel.prelevementId
    , adh.adhesionId, u.firstName, u.lastName, u.matriculeFonctionnaire, u.email, u.tel
    , ech.echeanceId, ech.nomEcheance, cot.cotisationId, cot.nomCotisation, cot.motif
    )
    from DefautPrelevement dp 
        join dp.adhesion adh
        join adh.member u
        join dp.prelevement prel
        join prel.echeance ech
        join prel.cotisation cot
    where dp.defautPrelevementId = ?1
""")
    DefautPrelevementDTO getDefautPrelevementDtoById(Long defautPrelevementId);

    @Query("""
    select new rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.DefautPrelevementDTO(
    dp.defautPrelevementId, dp.montant, dp.montantLettre, dp.motif, dp.active, prel.prelevementId
    , adh.adhesionId, u.firstName, u.lastName, u.matriculeFonctionnaire, u.email, u.tel
    , ech.echeanceId, ech.nomEcheance, cot.cotisationId, cot.nomCotisation, cot.motif
    )
    from DefautPrelevement dp 
        join dp.adhesion adh
        join adh.member u
        join dp.prelevement prel
        join prel.echeance ech
        join prel.cotisation cot
    where prel.prelevementId = ?1
""")
    Page<DefautPrelevementDTO> getDefautPrelevemenListByPrelevementId(Long prelevementId, Pageable pageable);
}