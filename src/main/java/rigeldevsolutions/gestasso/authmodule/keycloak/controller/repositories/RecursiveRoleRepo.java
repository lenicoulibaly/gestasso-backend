package rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.RecursiveRole;

import java.util.List;

public interface RecursiveRoleRepo extends JpaRepository<RecursiveRole, Long>
{
    @Query("SELECT rr.rolePath FROM RecursiveRole rr WHERE rr.clientUuid = ?1 AND rr.roleName = ?2")
    String findRolePathByClientIdAndRoleName(String clientId, String roleName);

    @Query("SELECT rr FROM RecursiveRole rr WHERE rr.clientUuid = ?1 AND LOCATE(?2, rr.rolePath) = 1")
    List<RecursiveRole> findAllSubRolesByRolePath(String clientId, String parentRolePath);

    @Query("SELECT rr.roleName FROM RecursiveRole rr WHERE rr.clientUuid = ?1 AND LOCATE(?2, rr.rolePath) = 1")
    List<String> findAllSubRoleNamesByRolePath(String clientId, String parentRolePath);

}