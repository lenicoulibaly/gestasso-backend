package rigeldevsolutions.gestasso.authmodule.keycloak.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProfileDto
{
    private String id;
    private String name;
    private boolean active;
    private List<ProfileDto> roles;
    private List<ProfileDto> privileges;
}
