package rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction;

import com.fasterxml.jackson.annotation.JsonFormat;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appuser.ExistingUserId;
import rigeldevsolutions.gestasso.authmodule.model.dtos.asignation.CoherentDates;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CoherentDates
public class UpdateFncDTO
{
    @ExistingFncId
    private Long fncId;
    @ExistingUserId
    private Long userId;
    private Long assoId;
    private Long sectionId;
    private String typeCode;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startsAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endsAt;
    private Set<String> roleCodes = new HashSet<>();

    public UpdateFncDTO(Long fncId, Long userId, Long assoId, Long sectionId, String typeCode, String name, LocalDate startsAt, LocalDate endsAt) {
        this.fncId = fncId;
        this.userId = userId;
        this.assoId = assoId;
        this.sectionId = sectionId;
        this.typeCode = typeCode;
        this.name = name;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }
}