package rigeldevsolutions.gestasso.metier.assomodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.authmodule.model.entities.HistoDetails;
import rigeldevsolutions.gestasso.structuremodule.model.entities.Structure;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited @EntityListeners(AuditingEntityListener.class)
@Entity
public class Section extends HistoDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECTION_ID_GEN")
    @SequenceGenerator(name = "SECTION_ID_GEN", sequenceName = "SECTION_ID_GEN", allocationSize = 10)
    private Long sectionId;
    private String sectionName;
    private String situationGeo;
    private String sigle;
    @ManyToOne @JoinColumn(name = "ASSOCIATION_ID")
    private Association association;

    @ManyToOne @JoinColumn(name = "STR_ID")
    private Structure strTutelle;

    public Section(Long sectionId) {
        this.sectionId = sectionId;
    }

    @Override
    public String toString() {
        return sectionId + "_" + sectionName + '_' + sigle;
    }
}
