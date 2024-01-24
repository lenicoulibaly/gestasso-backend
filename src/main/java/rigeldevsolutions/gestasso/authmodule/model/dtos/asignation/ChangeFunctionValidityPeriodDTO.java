package rigeldevsolutions.gestasso.authmodule.model.dtos.asignation;

import rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction.ExistingFncId;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChangeFunctionValidityPeriodDTO
{
    @ExistingFncId
    private Long fncId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
