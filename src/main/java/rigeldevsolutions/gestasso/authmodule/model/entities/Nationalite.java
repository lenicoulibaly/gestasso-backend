package rigeldevsolutions.gestasso.authmodule.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Table(name = "nationalite") @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Nationalite
{
    @Id
    @Column(name = "code_pays")
    private String codePays;
    @Column(name = "nationalite", unique = true)
    private String nationalite;
    @Column(name = "nom_pays", unique = true)
    private String nomPays;

    public Nationalite(String codePays) {
        this.codePays = codePays;
    }
}