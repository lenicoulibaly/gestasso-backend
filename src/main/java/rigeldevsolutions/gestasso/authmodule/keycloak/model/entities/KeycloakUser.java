package rigeldevsolutions.gestasso.authmodule.keycloak.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "v_user")
public class KeycloakUser
{
    @Id
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String tel;
    private String matricule;
    private boolean emailVerified;
    private Long createdTimestamp;
    private boolean enabled;
    private String realmId;
    private String codeCivilite;
    private int indice;
    private String lieuNaissance;
    private LocalDate dateNaissance;
    private String paysCode;
    private String gradeCode;
    @Transient
    private Map<String, Object> attributes;

    public KeycloakUser(String id, String firstName, String lastName, String email, String tel, String matricule, String codeCivilite, int indice, String lieuNaissance, LocalDate dateNaissance, String paysCode, String gradeCode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tel = tel;
        this.matricule = matricule;
        this.codeCivilite = codeCivilite;
        this.indice = indice;
        this.lieuNaissance = lieuNaissance;
        this.dateNaissance = dateNaissance;
        this.paysCode = paysCode;
        this.gradeCode = gradeCode;
    }

    public KeycloakUser(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + "(" + email + " - " + tel + " - " +matricule +")";
    }
}
