package rigeldevsolutions.gestasso.metier.assomodule.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.metier.assomodule.model.validators.UniqueAssoName;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class CreateAssociationDTO
{
    @NotNull(message = "Le nom de l'association est obligatoire")
    @NotBlank(message = "Le nom de l'association est obligatoire")
    @UniqueAssoName
    private String assoName;
    private String situationGeo;
    private String sigle;
    private BigDecimal droitAdhesion;
    //@Valid
    private List<CreateSectionDTO> createSectionDTOS;
}