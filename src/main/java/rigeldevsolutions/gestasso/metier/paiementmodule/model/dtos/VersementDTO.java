package rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators.ExistingCotisationId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VersementDTO
{
    private Long versementId;
    private String codeVersement;
    private LocalDate dateVersement;
    private BigDecimal montant;
    private String montantLettre;
    private boolean active;
    private String modePaiementCode;
    private String modePaiement;
    private String typePaiementCode;
    private String typePaiement;
    private Long adhesionId;
    private String firstName;
    private String lastName;
    private String matricule;
    private String email;
    private String tel;
    @ExistingCotisationId
    private Long cotisationId;
    private String nomCotisation;
    private String motif;
    private Long assoId;
    private String assoName;
    private String assoSigle;
    List<PaiementCotisationDTO> paiements;

    public VersementDTO(Long versementId, String codeVersement, LocalDate dateVersement, BigDecimal montant, String montantLettre, String modePaiement, String firstName, String lastName, String matricule, String email, String tel, String nomCotisation, String motif, Long assoId, String assoName, String assoSigle) {
        this.versementId = versementId;
        this.codeVersement = codeVersement;
        this.dateVersement = dateVersement;
        this.montant = montant;
        this.montantLettre = montantLettre;
        this.modePaiement = modePaiement;
        this.firstName = firstName;
        this.lastName = lastName;
        this.matricule = matricule;
        this.email = email;
        this.tel = tel;
        this.nomCotisation = nomCotisation;
        this.motif = motif;
        this.assoId = assoId;
        this.assoName = assoName;
        this.assoSigle = assoSigle;
    }
}
