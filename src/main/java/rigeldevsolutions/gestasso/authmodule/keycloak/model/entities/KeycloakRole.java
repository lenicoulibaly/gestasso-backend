package rigeldevsolutions.gestasso.authmodule.keycloak.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.dtos.KeycloakAttribute;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "v_role")
public class KeycloakRole
{
    @Id
    private String id;
    private String name;
    private String description;
    private String realmId;
    private String realm;
    private String clientPk;
    private String clientName;
    private String type;
    private boolean composite;
    @Transient
    private KeycloakAttribute attributes;

    public KeycloakRole(String name, String description, KeycloakAttribute creationType) {
        this.name = name;
        this.description = description;
        this.attributes = creationType;
    }
}
