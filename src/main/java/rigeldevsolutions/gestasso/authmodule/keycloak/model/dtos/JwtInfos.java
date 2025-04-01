package rigeldevsolutions.gestasso.authmodule.keycloak.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class JwtInfos
{
    private String userId;
    private String userEmail;
    private List<String> privileges;
    private List<String> roles;
    private List<String> profiles;
    private List<String> authorities;
    private String profileId;
    private String profileName;
    private Date tokenStartingDate;
    private Date tokenEndingDate;
    private String connectionId;
    private Long assoId;
    private String assoName;
    private Long sectionId;
    private String sectionName;
    private Long strId;
    private String strCode;
    private String strName;
    private String strSigle;
}
