package rigeldevsolutions.gestasso.authmodule.model.dtos.appuser;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@UniqueTel
public class UpdateUserDTO
{
    @ExistingUserId
    private Long userId;
    private String tel;
    private String firstName;
    private String lastName;
}
