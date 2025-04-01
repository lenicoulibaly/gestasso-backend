package rigeldevsolutions.gestasso.authmodule.keycloak.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "v_recursive_role")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RecursiveRole
{
    @Id
    private Long rId;
    private String roleId;
    private String roleName;
    private String description;
    private String realmId;
    private String clientUuid;
    private String parentRoleId;
    private String rolePath;
    private String type;
}
