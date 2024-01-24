package rigeldevsolutions.gestasso.archivemodule.model.entities;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section;
import rigeldevsolutions.gestasso.modulestatut.entities.Statut;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Audited @EntityListeners(AuditingEntityListener.class)
public class Document
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOC_ID_GEN")
	@SequenceGenerator(name = "DOC_ID_GEN", sequenceName = "DOC_ID_GEN")
	private Long docId;
	private String docNum;
	private String docName;
	@Column(length = 10000)
	private String docDescription;
	private String docPath;

	@ManyToOne @JoinColumn(name = "TYPE_ID")
	private Type docType;
	@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "USER_ID")
	private AppUser user;
	@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ASSOCIATION_ID")
	private Association association;
	@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "SECTION_ID")
	private Section section;


	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	@ManyToOne @JoinColumn(name = "STA_CODE")
	private Statut status;

	@Transient
	private MultipartFile file;
}
