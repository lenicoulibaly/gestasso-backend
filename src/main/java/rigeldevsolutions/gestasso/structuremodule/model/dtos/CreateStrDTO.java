package rigeldevsolutions.gestasso.structuremodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import rigeldevsolutions.gestasso.typemodule.model.dtos.ExistingTypeCode;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor //@Entity
@CompatibleTypeAndParentStr
public class CreateStrDTO
{
    @Length(message = "Le nom de la structure doit contenir au moins 3 caractères", min = 3)
    @NotNull(message = "Le nom de la structure ne peut être nul")
    private String strName;
    @NotNull(message = "Le sigle de la structure ne peut être nul")
    private String strSigle;
    @NotNull(message = "Le type de la structure ne peut être nul")
    @ExistingTypeCode
    private String typeCode;
    @ExistingOrNullStrId
    private Long parentId;

    private String strTel;
    private String strAddress;
    @NotNull(message = "La situation géographique ne peut être nulle")
    private String situationGeo;
    private MultipartFile creationActFile;

    @Override
    public String toString()
    {
        return this.strName + " ("+this.strSigle + ")";
    }

}
