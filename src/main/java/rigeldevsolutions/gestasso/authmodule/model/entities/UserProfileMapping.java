package rigeldevsolutions.gestasso.authmodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Audited
public class UserProfileMapping
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_PROFILE_ID_GEN")
    @SequenceGenerator(name = "USER_PROFILE_ID_GEN", sequenceName = "USER_PROFILE_ID_GEN", allocationSize = 10)
    private Long id;
    private String userId;
    private String profileId;
    private Long assoId;
    private Long sectionId;
    private boolean active;

    public UserProfileMapping(String userId, String profileId, Long assoId)
    {
        this.userId = userId;
        this.profileId = profileId;
        this.assoId = assoId;
        this.active = false;
    }

    public UserProfileMapping(String userId, String profileId, Long assoId, Long sectionId)
    {
        this.userId = userId;
        this.profileId = profileId;
        this.assoId = assoId;
        this.sectionId = sectionId;
        this.active = false;
    }
}