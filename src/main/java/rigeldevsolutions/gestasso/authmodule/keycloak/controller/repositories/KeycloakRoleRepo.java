package rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakRole;

import java.util.List;
import java.util.Optional;

public interface KeycloakRoleRepo extends JpaRepository<KeycloakRole, String>
{
    @Query("""
        select kr from KeycloakRole kr where kr.clientPk = ?1 and kr.id = ?2
    """)
    KeycloakRole findByClientUuIdAndRoleId(String keycloakClientUuid, String id);
    @Query("""
        select kr from KeycloakRole kr where kr.clientPk = ?1 and kr.name in ?2
    """)
    List<KeycloakRole> findByClientUuIdAndRoleNames(String clientUuid, List<String> roleNames);

    @Query("""
        select kr from KeycloakRole kr where kr.clientPk = ?1 and kr.name = ?2
    """)
    Optional<KeycloakRole> findByClientUuIdAndRoleName(String clientUuid, String roleName);

    @Query("""
        select kr from KeycloakRole kr where kr.clientName = ?1 and upper(kr.type) = upper(?2)
    """)
    List<KeycloakRole> findByClientUuIdAndRoleType(String clientName, String type);

    @Query("select (count(r) > 0) from KeycloakRole r where r.clientPk = ?1 and  r.id = ?2 and r.type = 'PROFILE'")
    boolean profileExistsById(String clientUuid,  String profileId);

    @Query("select (count(r) > 0) from KeycloakRole r where r.clientPk = ?1 and  r.id = ?2")
    boolean authorityExistsById(String clientUuid,  String authorityId);


}