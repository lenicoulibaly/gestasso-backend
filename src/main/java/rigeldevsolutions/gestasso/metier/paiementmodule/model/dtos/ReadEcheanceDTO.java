package rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.entities.Exercice;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.enums.Frequence;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.ExistingEcheancierId;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ReadEcheanceDTO
{
    private Long echeanceId;
    private LocalDate dateEcheance;
    private LocalDate dateButtoire;
    private BigDecimal montantEcheance;
    private BigDecimal tauxEcheance;
    private Long echeancierId;
    private String echeancierTypeCode;
    private String echeancierTypeName;
    private String echeancierFrequence;
    private String echeancierName;
    private Long exeCode;
}
