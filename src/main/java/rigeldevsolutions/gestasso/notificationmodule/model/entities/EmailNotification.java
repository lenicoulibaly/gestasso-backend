package rigeldevsolutions.gestasso.notificationmodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class EmailNotification
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIF_ID_GEN")
    @SequenceGenerator(name = "NOTIF_ID_GEN", sequenceName = "NOTIF_ID_GEN", allocationSize = 10)
    private Long mailId;
    private String username;
    private String email; //Recipient Email
    private String token;
    private LocalDateTime sendingDate;
    private boolean seen;
    private boolean sent;
    private String mailObject;
    private String mailMessage;

    private Long senderUserId;

    private String systemMailSender; /* L'adresse mail utilisée pour envoyer le mail*/

    public EmailNotification(KeycloakUser user, String mailObject, String token, Long connectedUserId)
    {
        this.username = user.getFirstName();
        this.email = user.getEmail();
        this.sendingDate = LocalDateTime.now();
        this.token = token;
        this.mailObject = mailObject;
        this.senderUserId = connectedUserId;
    }
}
