package rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos;

import lombok.*;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.CoherentEcheanceDates;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.ExistingEcheancierId;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CoherentEcheanceDates
public class CreateEcheanceDTO
{
    private LocalDate dateEcheance;
    private LocalDate dateButtoire;
    private BigDecimal montantEcheance;
    private BigDecimal tauxEcheance;
    private String frequenceUniqueCode;
    @ExistingEcheancierId
    private Long echeancierId;

    public CreateEcheanceDTO(LocalDate dateEcheance, Long echeancierId, String frequenceUniqueCode) {
        this.dateEcheance = dateEcheance;
        this.echeancierId = echeancierId;
        this.frequenceUniqueCode = frequenceUniqueCode;
    }
}