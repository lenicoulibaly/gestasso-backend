package rigeldevsolutions.gestasso.authmodule.model.dtos.appuser;

import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter @NoArgsConstructor
public class AuthResponseDTO
{
    private String accessToken;
    private String refreshToken;
    private AppUser user;

    public AuthResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
