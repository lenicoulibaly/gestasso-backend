package rigeldevsolutions.gestasso.metier.assomodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;

public interface AssoRepo extends JpaRepository<Association, Long>
{
    @Query("""
    select new rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadAssociationDTO(
    a.assoId, a.assoName, a.situationGeo, a.sigle, a.droitAdhesion) 
    from Association a where
    (locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(a.assoName, '') ) as string))) >0 
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(a.situationGeo, '') ) as string))) >0
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce( a.assoName, '') ) as string))) >0
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(a.sigle, '') ) as string))) >0
           ) 
           and (a.isDeleted = false)
""")
    Page<ReadAssociationDTO> searchAssociations(@Param("key") String key, Pageable pageable);

    @Query("select s.strCode from Structure s where s.strId = :strId")
    String getStrCode(@Param("strId") Long strId);

    @Query("select (count(a.assoId)>0) from Association a where trim(upper(a.assoName)) = trim(upper(?1))")
    boolean existsByName(String assoName);
    @Query("select (count(a.assoId)>0) from Association a where trim(upper(a.assoName)) = trim(upper(?1)) and a.assoId <> ?2")
    boolean existsByName(String assoName, Long assoId);
}

