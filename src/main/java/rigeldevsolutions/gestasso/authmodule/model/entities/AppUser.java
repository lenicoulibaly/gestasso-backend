package rigeldevsolutions.gestasso.authmodule.model.entities;

import jakarta.persistence.*;
import rigeldevsolutions.gestasso.modulestatut.entities.Statut;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Audited @EntityListeners(AuditingEntityListener.class)
public class AppUser
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_GEN")
    @SequenceGenerator(name = "USER_ID_GEN", sequenceName = "USER_ID_GEN", allocationSize = 10)
    protected Long userId;
    protected String firstName;
    protected String lastName;
    protected String password;
    @Column(unique = true)
    protected String email;
    @Column(unique = true)
    protected String tel;
    protected String lieuNaissance;
    protected LocalDate dateNaissance;
    @ManyToOne @JoinColumn(name = "SECTION_ID")
    private Section section;
    @ManyToOne @JoinColumn(name = "ASSOCIATION_ID")
    private Association association;
    @ManyToOne @JoinColumn(name = "GENRE_CODE")
    protected Type civilite;
    @ManyToOne @JoinColumn(name = "nationalite_ID")
    protected Nationalite nationalite;
    @ManyToOne @JoinColumn(name = "TYPE_PIECE_CODE")
    protected Type typePiece;
    @Column(length = 50, unique = true)
    protected String numPiece;
    protected String nomPere;
    protected String nomMere;

    @ManyToOne @JoinColumn(name = "TYPE_USER_CODE")
    protected Type typeUtilisateur;
    protected boolean active;
    protected boolean notBlocked;
    protected Long currentFunctionId;
    protected LocalDateTime changePasswordDate;
    @ManyToOne
    protected Statut statut;

    @CreatedDate
    @Column(name = "CreatedAt")
    protected LocalDateTime createdAt;
    @CreatedBy
    @Column(name = "CreatedBy", length = 50)
    protected String createdBy;
    @LastModifiedDate
    @Column(name = "UpdatedAt")
    protected LocalDateTime updatedAt;
    @LastModifiedBy
    @Column(name = "UpdatedBy", length = 50)
    protected String updatedBy;
    @Column(name = "DeletedAt")
    protected LocalDateTime deletedAt;
    @Column(name = "DeletedBy", length = 50)
    protected String deletedBy;
    @Column(name = "isDeleted", length = 50)
    protected Boolean isDeleted = false;
    protected String action;
    protected String connectionId;

    public AppUser(Long userId, String firstName, String lastName, String password, String email, String tel, boolean active, boolean notBlocked, Long currentFunctionId, LocalDateTime changePasswordDate) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.active = active;
        this.notBlocked = notBlocked;
        this.currentFunctionId = currentFunctionId;
        this.changePasswordDate = changePasswordDate;
    }

    public AppUser(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(getUserId(), appUser.getUserId()) || Objects.equals(getEmail(), appUser.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password, email, tel, active, notBlocked, currentFunctionId);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "userId=" + userId +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", active=" + active +
                ", notBlocked=" + notBlocked +
                ", currentFunctionId=" + currentFunctionId +
                '}';
    }
}
