package rigeldevsolutions.gestasso.modulelog.model.dtos.mapper;

import rigeldevsolutions.gestasso.modulelog.model.dtos.response.ConnexionList;
import rigeldevsolutions.gestasso.modulelog.model.entities.Log;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LogMapper {
    ConnexionList mapConnexionListToLog(Log log);
}
