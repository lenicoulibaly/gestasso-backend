package rigeldevsolutions.gestasso.metier.assomodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.response.ReadDocDTO;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadAssociationDTO
{
    private Long assoId;
    private String assoName;
    private String situationGeo;
    private String sigle;
    private BigDecimal droitAdhesion;
    private ReadDocDTO logo;


    public ReadAssociationDTO(Long assoId, String assoName, String situationGeo, String sigle, BigDecimal droitAdhesion)
    {
        this.assoId = assoId;
        this.assoName = assoName;
        this.situationGeo = situationGeo;
        this.sigle = sigle;
        this.droitAdhesion = droitAdhesion;
    }
}
