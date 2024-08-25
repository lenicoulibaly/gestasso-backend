package rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ReadEcheanceDTO2
{
    private Long echeanceId;
    private LocalDate dateEcheance;
    private LocalDate dateButtoire;
    private BigDecimal montantEcheance;
    private BigDecimal tauxEcheance;
    private Long echeancierId;
}
