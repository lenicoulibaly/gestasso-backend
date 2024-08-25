package rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.authmodule.model.entities.HistoDetails;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Audited
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Cotisation  extends HistoDetails {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADHESION_ID_GEN")
    @SequenceGenerator(name = "ADHESION_ID_GEN", sequenceName = "ADHESION_ID_GEN", allocationSize = 10)
    private Long cotisationId;
    private String nomCotisation;
    private BigDecimal montantCotisation;
    @ManyToOne @JoinColumn(name = "frequence_cotisation_unique_code")
    private Type frequenceCotisation;
    @ManyToOne @JoinColumn(name = "mode_prelevement_unique_code")
    private Type modePrelevement;
    private LocalDate dateDebutCotisation;
    private LocalDate dateFinCotisation;
    private int delaiDeRigueurEnJours;
    @ManyToOne @JoinColumn(name = "asso_id")
    private Association association;
    @ManyToOne @JoinColumn(name = "section_id")
    private Section section;
}