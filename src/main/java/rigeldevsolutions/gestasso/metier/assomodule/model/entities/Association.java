package rigeldevsolutions.gestasso.metier.assomodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.enums.Frequence;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited @EntityListeners(AuditingEntityListener.class)
@Entity
public class Association
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ASSO_ID_GEN")
    @SequenceGenerator(name = "ASSO_ID_GEN", sequenceName = "ASSO_ID_GEN", allocationSize = 10)
    private Long assoId;
    private String assoName;
    private BigDecimal montantCotisation;
    @Enumerated(EnumType.STRING)
    private Frequence frequenceCotisation;

    public Association(Long assoId) {
        this.assoId = assoId;
    }
}
