package rigeldevsolutions.gestasso.metier.assomodule.model.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.ExistingAssoId;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateAssociationDTO
{
    @ExistingAssoId
    private Long assoId;
    @NotNull(message = "Le nom de l'association ne peut être null")
    private String assoName;
    private String sigle;
    private String situationGeo;
    private BigDecimal droitAdhesion;
}
