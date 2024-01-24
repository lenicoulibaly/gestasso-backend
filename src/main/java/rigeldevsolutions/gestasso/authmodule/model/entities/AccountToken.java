package rigeldevsolutions.gestasso.authmodule.model.entities;

import lombok.*;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class AccountToken
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOKEN_ID_GEN")
    @SequenceGenerator(name = "TOKEN_ID_GEN", sequenceName = "TOKEN_ID_GEN", allocationSize = 10)
    private Long tokenId;
    @Column(unique = true)
    private String token;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
    private LocalDateTime usageDate;
    private boolean alreadyUsed;
    private Long agentId; // Très important pour lier le token à un agent (acteur) au cas où user est null
    private String password; // Mot de passe d'utilisation du token aléatoirement généré!
    private boolean emailSent;
    @ManyToOne
    private AppUser user;

    @CreatedDate
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
    @CreatedBy
    @Column(name = "CreatedBy", length = 50)
    private String createdBy;
    @LastModifiedDate
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
    @LastModifiedBy
    @Column(name = "UpdatedBy", length = 50)
    private String updatedBy;
    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;
    @Column(name = "DeletedBy", length = 50)
    private String deletedBy;
    @Column(name = "isDeleted", length = 50)
    private Boolean isDeleted = false;
    private String action;
    private String connectionId;

    public AccountToken(Long tokenId)
    {
        this.tokenId = tokenId;
    }

    public AccountToken(String token, AppUser user)
    {
        this.token = token;
        this.emailSent = false;
        this.alreadyUsed = false;
        this.user = user;
        this.creationDate = LocalDateTime.now();
        this.expirationDate = LocalDateTime.now().plusDays(1);
    }
}
