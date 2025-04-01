package rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.AdhesionDTO;
import rigeldevsolutions.gestasso.sharedmodule.dtos.SelectOption;

import java.util.List;

public interface KeycloakUserRepo extends JpaRepository<KeycloakUser, String>
{
    @Query("""
        select u from KeycloakUser u
        where
            (locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(u.firstName, '') ) as string))) >0 
               or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.lastName, '') ) as string))) >0
               or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.email, '') ) as string))) >0
               or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.username, '') ) as string))) >0
               or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.tel, '') ) as string) )) >0)
    """)
    Page<KeycloakUser> search(@Param("key") String key, Pageable pageable);

    @Query("select (count(u) > 0) from KeycloakUser u where upper(u.matricule) = upper(?1)")
    boolean existsByMatricule(String matricule);

    @Query("select (count(u) > 0) from KeycloakUser u where upper(u.matricule) = upper(?1) and u.id <> ?2")
    boolean existsByMatricule(String email, String userId);

    @Query("select (count(u)>0) from KeycloakUser u where upper(u.email) = upper(?1) and u.id <> ?2")
    boolean existsByEmail(String email, String userId);

    @Query("select (count(u)>0) from KeycloakUser u where upper(u.email) = upper(?1)")
    boolean existsByEmail(String email);

    @Query("select (count(u)>0) from KeycloakUser u where upper(u.tel) = upper(?1)")
    boolean existsByTel(String tel);

    @Query("select (count(u)>0) from KeycloakUser u where upper(u.tel) = upper(?1) and u.id <> ?2")
    boolean existsByTel(String tel, String userId);

    @Query("select (count(ku)>0) from KeycloakUser ku where ku.realmId = ?1 and ku.id = ?2")
    boolean existsByUserId(String realm, String userId);

    @Query("""
    select rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser
    (u.id, u.firstName, u.lastName, u.email, u.tel, u.matricule, u.codeCivilite, u.indice, u.lieuNaissance, u.dateNaissance, u.paysCode, u.gradeCode)
    from KeycloakUser u where u.matricule = ?1 or u.email = ?1 or u.tel = ?1
""")
    KeycloakUser findByIdIdentifiant(String uniqueIdentifier);

    @Query("""
    select new rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser
    (u.id, u.firstName, u.lastName, u.email, u.tel, u.matricule, u.codeCivilite, u.indice, u.lieuNaissance, u.dateNaissance, u.paysCode, u.gradeCode)
    from KeycloakUser u where u.matricule = ?1
""")
    KeycloakUser findByMatricule(String matricule);

    @Query("""
    select new rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser
    (u.id, u.firstName, u.lastName, u.email, u.tel, u.matricule, u.codeCivilite, u.indice, u.lieuNaissance, u.dateNaissance, u.paysCode, u.gradeCode)
    from KeycloakUser u where u.email = ?1
""")
    KeycloakUser findByEmail(String email);

    @Query("""
    select new rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser
    (u.id, u.firstName, u.lastName, u.email, u.tel, u.matricule, u.codeCivilite, u.indice, u.lieuNaissance, u.dateNaissance, u.paysCode, u.gradeCode)
    from KeycloakUser u where u.tel = ?1
""")
    KeycloakUser findByTel(String tel);

    @Query("select u from KeycloakUser u where u.email= ?1 and u.realmId = ?2")
    String findUserIdByEmail(String emil, String realmId);

    @Query("""
    select u.id from KeycloakUser u where 
    (locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(u.username, '') ) as string))) >0 or
    locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(u.firstName, '') ) as string))) >0 or
    locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(u.lastName, '') ) as string))) >0 or
    locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(u.email, '') ) as string))) >0 or
    locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(u.tel, '') ) as string))) >0 or
    locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(u.matricule, '') ) as string))) >0)
    and u.realmId = :realmId
    """)
    List<String> searchUserIds(@Param("key") String key, @Param("realmId") String realmId);

}
