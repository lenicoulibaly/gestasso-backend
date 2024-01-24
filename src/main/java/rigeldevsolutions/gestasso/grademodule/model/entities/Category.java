package rigeldevsolutions.gestasso.grademodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Category
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String categoryName;

    public Category(Long categoryId) {
        this.categoryId = categoryId;
    }
}
