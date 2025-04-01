package rigeldevsolutions.gestasso.authmodule.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import rigeldevsolutions.gestasso.authmodule.model.entities.UserProfileMapping;

import java.util.Optional;

public interface UserProfileMappingRepo extends JpaRepository<UserProfileMapping, Long>
{
    @Query("select u from UserProfileMapping u where u.userId = ?1 and u.profileId = ?2 and u.assoId = coalesce(?3, u.assoId) and u.sectionId = coalesce(?3, u.assoId)")
    Optional<UserProfileMapping> findByUserIdAndProfileId(String userId, String profileId, Long assoId, Long sectionId);

    @Modifying
    @Query("delete from UserProfileMapping u where u.userId = ?1 and u.profileId = ?2 and u.assoId = coalesce(?3, u.assoId) and u.sectionId = coalesce(?3, u.assoId)")
    boolean removeByUserIdAndProfileId(String userId, String profileId, Long assoId, Long sectionId);

    @Modifying
    @Query("update UserProfileMapping upm set upm.active = false where upm.userId = ?1 and upm.profileId <> ?2")
    void setOtherAsNoneActive(String userId, String profileId);

    @Query("select upm from UserProfileMapping upm where upm.userId = ?1 and upm.active")
    UserProfileMapping findActiveByUser(String userId);

    @Query("select (count(upm)>0) from UserProfileMapping upm where upm.userId = ?1 and upm.profileId = ?2 and upm.assoId = coalesce(?3, upm.assoId) and upm.sectionId = coalesce(?3, upm.assoId) and upm.active")
    boolean hasActiveProfile(String userId, String profileId, Long assoId, Long sectionId);

    @Query("select (count(upm)>0) from UserProfileMapping upm where upm.userId = ?1 and upm.active")
    boolean hasAnyActiveProfile(String userId);
}