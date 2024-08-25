package rigeldevsolutions.gestasso.metier.assomodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.enums.Frequence;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadAssociationDTO
{
    private Long assoId;
    private String assoName;
    private String situationGeo;
    private String sigle;
    private BigDecimal droitAdhesion;

    public ReadAssociationDTO(Long assoId, String assoName, String situationGeo, String sigle, BigDecimal montantCotisation, BigDecimal droitAdhesion, Frequence frequenceCotisation, String strName, String strSigle) {
        this.assoId = assoId;
        this.assoName = assoName;
        this.situationGeo = situationGeo;
        this.sigle = sigle;
        this.droitAdhesion = droitAdhesion;
    }
}
