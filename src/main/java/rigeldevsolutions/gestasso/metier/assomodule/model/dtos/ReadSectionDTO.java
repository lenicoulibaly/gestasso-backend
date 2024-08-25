package rigeldevsolutions.gestasso.metier.assomodule.model.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadSectionDTO
{
    private Long sectionId;
    private String sectionName;
    private String situationGeo;
    private String sigle;
    private Long assoId;
    private String assoName;
    private String strName;
    private String strSigle;
}
