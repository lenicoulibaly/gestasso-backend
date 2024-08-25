package rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos;

import lombok.*;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.CoherentEcheanceDates;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.ExistingEcheanceId;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.ExistingEcheancierId;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CoherentEcheanceDates
public class UpdateEcheanceDTO
{
    @ExistingEcheanceId
    private Long echeanceId;
    private LocalDate dateEcheance;
    private LocalDate dateButtoire;
    private BigDecimal montantEcheance;
    @ExistingEcheancierId
    private Long echeancierId;
}