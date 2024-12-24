package rigeldevsolutions.gestasso.metier.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "v_echeance_cotisation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EcheanceCotisation
{
    @Id
    @Column(name = "rownum")
    private Long rownum;
    @Column(name = "cotisation_id")
    private Long cotisationId;
    @Column(name = "nom_cotisation")
    private String nomCotisation;
    @Column(name = "montant_cotisation")
    private Double montantCotisation;
    @Column(name = "delai_de_rigueur_en_jours")
    private Integer delaiDeRigueurEnJours;
    @Column(name = "date_debut_cotisation")
    private LocalDate dateDebutCotisation;
    @Column(name = "date_fin_cotisation")
    private LocalDate dateFinCotisation;
    @Column(name = "echeancier_id")
    private Long echeancierId;
    @Column(name = "echeancier")
    private String echeancier;
    @Column(name = "echeance_id")
    private Long echeanceId;
    @Column(name = "date_echeance")
    private LocalDate dateEcheance;
    @Column(name = "echeance_echu")
    private Boolean echeanceEchu;
    @Column(name = "delai_echu")
    private Boolean delaiEchu;
    private String nomEcheance;
}