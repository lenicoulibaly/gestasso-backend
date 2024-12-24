package rigeldevsolutions.gestasso.metier.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface EcheanceCotisationRepo extends JpaRepository<EcheanceCotisation, Long>
{
    @Query("""
        select sum (ec.montantCotisation) from EcheanceCotisation ec where ec.cotisationId = ?1 and ec.dateEcheance <= ?2
""")
    BigDecimal calculateMontantStrictAttenduATerme(Long cotisationId, LocalDate date);
}