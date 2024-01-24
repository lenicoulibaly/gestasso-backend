package rigeldevsolutions.gestasso.notificationmodule.model.dto.mappers;

import rigeldevsolutions.gestasso.notificationmodule.model.entities.EmailNotification;
import rigeldevsolutions.gestasso.notificationmodule.model.dto.EmailNotificationDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EMailNotificationMapper
{
    EmailNotification mapToNotification(EmailNotificationDTO dto);
}
