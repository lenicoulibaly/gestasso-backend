package rigeldevsolutions.gestasso.metier.assomodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.AdhesionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;
import rigeldevsolutions.gestasso.sharedmodule.dtos.SelectOption;

import java.util.List;
import java.util.Optional;

public interface AdhesionRepo extends JpaRepository<Adhesion, Long>
{
    @Query("""
        select new rigeldevsolutions.gestasso.metier.assomodule.model.dtos.AdhesionDTO(
        a.userId, sect.sectionId, asso.assoId, a.adhesionId, sect.sectionName, asso.assoName) 
        from Adhesion a left join a.section sect left join sect.association sectAsso 
        left join a.association asso 
        where 
        (locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(sect.sectionName, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(asso.assoName, '') ) as string))) >0)
        and (:usersIds is null or a.userId in :usersIds)
        and ((asso.assoId = :assoId or sectAsso.assoId = :assoId) or (sect.sectionId = :sectionId))
""")
    Page<AdhesionDTO>

    searchAdhsions(@Param("key") String key,
                   @Param("usersIds") List<String> usersIds,
                   @Param("assoId") Long assoId,
                   @Param("sectionId") Long sectionId,
                   Pageable pageable);




    @Query("select a from Adhesion a where a.userId = ?1 and a.section.sectionId = ?2")
    Optional<Adhesion> findByUserIdAndSectionId(String keycloakUserId, Long sectionId);

    @Query("select a from Adhesion a where a.userId = ?1 and a.association.assoId = ?2")
    Optional<Adhesion> findByUserIdAndAsso(String keycloakUserId, Long assoId);


    @Query("select a from Adhesion a where a.association.assoId = ?1")
    List<Adhesion> getAdhesionsByAssoId(Long assoId);
}