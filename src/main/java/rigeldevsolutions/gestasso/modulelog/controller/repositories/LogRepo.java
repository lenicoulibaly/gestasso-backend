package rigeldevsolutions.gestasso.modulelog.controller.repositories;

import rigeldevsolutions.gestasso.modulelog.model.dtos.response.ConnexionList;
import rigeldevsolutions.gestasso.modulelog.model.entities.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LogRepo extends JpaRepository<Log, Long>
{
    @Query("""
        select new rigeldevsolutions.gestasso.modulelog.model.dtos.response.ConnexionList(l.id,
        l.userId, l.userEmail, u.firstName, u.lastName, l.action, l.actionDateTime, 
        l.ipAddress, l.hostName, l.connectionId, f.id, f.name, l.errorMessage, l.stackTrace)
        from Log l left join l.function f left join f.user u left join f.association a left join f.section s where 
        (locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(l.userEmail, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.firstName, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.lastName, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(l.action, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(f.name, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(l.ipAddress, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(l.hostName, '') ) as string))) >0
      
        )
        and l.action in :actions and (:connId is null or :connId = l.connectionId)
        and (:userId is null or :userId = l.userId) 
        and l.actionDateTime between coalesce(:debut, current_date) and coalesce(:fin, current_date)
        and (:assoId is null or :assoId = a.assoId)
        and (:sectionId is null or :sectionId = s.sectionId) 
        order by l.actionDateTime desc
""")
    Page<ConnexionList> getConnnexionLogs(@Param("actions") List<String> actions,
                                          @Param("key") String key,
                                          @Param("userId") Long userId,
                                          @Param("debut") LocalDate debut,
                                          @Param("fin") LocalDate fin,
                                          @Param("connId") String connectionId,
                                          @Param("assoId") Long assoId,
                                          @Param("sectionId") Long sectionId,
                                          Pageable pageable);
    @Query("select distinct l.action from Log l")
    List<String> getAllActionTypes();
}
