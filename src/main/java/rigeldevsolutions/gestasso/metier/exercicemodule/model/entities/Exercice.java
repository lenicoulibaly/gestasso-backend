package rigeldevsolutions.gestasso.metier.exercicemodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.authmodule.model.entities.HistoDetails;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
@Audited  @EntityListeners(AuditingEntityListener.class)
public class Exercice extends HistoDetails {
    @Id
    private Long exeCode;
    private String exeLibelle;
    private boolean exeCourant;

    public Exercice(Long exeCode) {
        this.exeCode = exeCode;
    }
}
