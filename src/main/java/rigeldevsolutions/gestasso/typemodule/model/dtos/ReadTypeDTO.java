package rigeldevsolutions.gestasso.typemodule.model.dtos;

import rigeldevsolutions.gestasso.sharedmodule.enums.PersStatus;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;
import rigeldevsolutions.gestasso.typemodule.model.enums.TypeGroup;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class ReadTypeDTO
{
    private String typeGroup;
    private String uniqueCode;
    private String name;
    private String status;
    private String objectFolder;
    private List<ReadTypeDTO> children;

    public ReadTypeDTO(String uniqueCode, TypeGroup typeGroup, String name, PersStatus status) {

        this.typeGroup = typeGroup.name();
        this.uniqueCode = uniqueCode;
        this.name = name;
        this.status = status.name();
    }

    public ReadTypeDTO(String uniqueCode, TypeGroup typeGroup, String name, PersStatus status, String objectFolder) {

        this.typeGroup = typeGroup.name();
        this.uniqueCode = uniqueCode;
        this.name = name;
        this.status = status.name();
        this.objectFolder = objectFolder;
    }

    public ReadTypeDTO(Type type) {
        BeanUtils.copyProperties(type, this);
    }
}