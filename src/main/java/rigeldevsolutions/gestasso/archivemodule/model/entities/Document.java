package rigeldevsolutions.gestasso.archivemodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;
import rigeldevsolutions.gestasso.authmodule.model.entities.HistoDetails;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Versement;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.entities.Prelevement;
import rigeldevsolutions.gestasso.modulestatut.entities.Statut;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Audited @EntityListeners(AuditingEntityListener.class)
@Table(name = "document")
public class Document extends HistoDetails
{
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOC_ID_GEN")
	@SequenceGenerator(name = "DOC_ID_GEN", sequenceName = "DOC_ID_GEN")
	@Column(name = "doc_id")
	private Long docId;
	@Column(name = "doc_num")
	private String docNum;
	@Column(name = "doc_name")
	private String docName;
	@Column(name = "doc_description", length = 10000)
	private String docDescription;
	@Column(name = "doc_path")
	private String docPath;
	@Column(name = "doc_extension")
	private String docExtension;
	@Column(name = "doc_mime_type")
	private String docMimeType;
	@ManyToOne @JoinColumn(name = "TYPE_ID")
	private Type docType;
	@Transient
	private KeycloakUser user;
	private String userId;
	@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ASSOCIATION_ID")
	private Association association;
	@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "SECTION_ID")
	private Section section;
	@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "VERSEMENT_ID")
	private Versement versement;
	@ManyToOne @JoinColumn(name = "PRELEVEMENT_ID") private
	Prelevement prelevement;
	@ManyToOne @JoinColumn(name = "STA_CODE") private Statut status;


	@Transient
	private MultipartFile file;
}
