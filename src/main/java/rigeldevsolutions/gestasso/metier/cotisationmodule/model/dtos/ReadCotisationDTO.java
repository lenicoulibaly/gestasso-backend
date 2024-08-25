package rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ReadCotisationDTO {
    private Long cotisationId;
    private String nomCotisation;
    private String montantCotisation;
    private String frequenceCotisation;
    private String frequenceCotisationCode;
    private String modePrelevement;
    private String modePrelevementCode;
    private LocalDate dateDebutCotisation;
    private LocalDate dateFinCotisation;
    private int delaiDeRigueurEnJours;
    private String assoName;
    private String assoSigle;
    private Long assoId;

    private String sectionName;
    private String sectionSigle;
    private Long sectionId;

    private BigDecimal motantAttendu;
    private BigDecimal montantEncaisse;
    private BigDecimal montantRestantAEncaisser;
    private BigDecimal montantEnRetardDeRecouvrement;
    private BigDecimal tauxDeRecouvrementTotal;
    private BigDecimal tauxDeRecouvrementPeriodique;

    public ReadCotisationDTO(Long cotisationId, String nomCotisation,
                             String montantCotisation,
                             String frequenceCotisation,
                             String frequenceCotisationCode,
                             String modePrelevement,
                             String modePrelevementCode,
                             LocalDate dateDebutCotisation,
                             LocalDate dateFinCotisation,
                             int delaiDeRigueurEnJours,
                             String assoName, String assoSigle,
                             Long assoId, String sectionName,
                             String sectionSigle, Long sectionId) {
        this.cotisationId = cotisationId;
        this.nomCotisation = nomCotisation;
        this.montantCotisation = montantCotisation;
        this.frequenceCotisation = frequenceCotisation;
        this.frequenceCotisationCode = frequenceCotisationCode;
        this.modePrelevement = modePrelevement;
        this.modePrelevementCode = modePrelevementCode;
        this.dateDebutCotisation = dateDebutCotisation;
        this.dateFinCotisation = dateFinCotisation;
        this.delaiDeRigueurEnJours = delaiDeRigueurEnJours;
        this.assoName = assoName;
        this.assoSigle = assoSigle;
        this.assoId = assoId;
        this.sectionName = sectionName;
        this.sectionSigle = sectionSigle;
        this.sectionId = sectionId;
    }
}
