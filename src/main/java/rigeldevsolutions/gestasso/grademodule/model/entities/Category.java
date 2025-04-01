package rigeldevsolutions.gestasso.grademodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "category")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "category_name")
    private String categoryName;

    public Category(Long categoryId)
    {
        this.categoryId = categoryId;
    }
}
