package rigeldevsolutions.gestasso.grademodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import rigeldevsolutions.gestasso.grademodule.model.enums.Categorie;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Grade 
{
	@Id
	private String gradeCode;
	private String nomGrade;
	private int rang; //D = 1 , C = 2 ,  B = 3 , A = 4
	@Enumerated(EnumType.STRING)
	private Categorie categorie;

	public Grade(String codeGrade) {
		this.gradeCode = codeGrade;
	}

	@Override
	public String toString() {
		return gradeCode;
	}
}