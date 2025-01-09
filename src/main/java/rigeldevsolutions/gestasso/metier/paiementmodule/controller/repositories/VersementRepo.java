package rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Versement;

public interface VersementRepo extends JpaRepository<Versement, Long>
{
    @Query("select v.cotisation.association.assoId from Versement v where v.versementId = ?1")
    Long getAssoIdByVersementId(Long versementId);

    @Query("""
    select new rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO(
    v.versementId, v.codeVersement, v.dateVersement, v.montant, v.montantLettre, v.modePaiement.name
    , m.firstName, m.lastName, m.matriculeFonctionnaire, m.email, m.tel, c.nomCotisation, c.motif, asso.assoId, asso.assoName, asso.sigle) 
    from Versement v join v.adhesion.member m join v.cotisation c join c.association asso
    where v.versementId = ?1
""")
    VersementDTO findVersmentById(Long versementId);
}