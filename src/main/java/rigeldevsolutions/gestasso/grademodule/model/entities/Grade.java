package rigeldevsolutions.gestasso.grademodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.grademodule.model.enums.Categorie;
import rigeldevsolutions.gestasso.sharedmodule.enums.PersStatus;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Grade 
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idGrade;
	private String nomGrade;
	private int rang; //D = 1 , C = 2 ,  B = 3 , A = 4
	@Enumerated(EnumType.STRING)
	private Categorie categorie;
	@Enumerated(EnumType.STRING)
	private PersStatus status;

	public Grade(Long idGrade) {
		this.idGrade = idGrade;
	}
}