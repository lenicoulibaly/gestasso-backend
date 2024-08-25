package rigeldevsolutions.gestasso.metier.assomodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section;

public interface SectionRepo extends JpaRepository<Section, Long>
{
    @Query("""
    select new rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadSectionDTO(
    s.sectionId, s.sectionName, s.situationGeo, s.sigle, a.assoId, a.assoName, str.strName, str.strSigle)
    from Section s left join s.association a left join s.strTutelle str where (
            locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(s.sectionName, '') ) as string))) >0 
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(s.sigle, '') ) as string))) >0
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(s.situationGeo, '') ) as string))) >0
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce( a.assoName, '') ) as string))) >0
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(a.sigle, '') ) as string))) >0
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(str.strName, '') ) as string))) >0
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(str.strSigle, '') ) as string))) >0
           )
           and (:assoId is null or :assoId = a.assoId) 
           and (:strId is null or :strId = str.strId) 
           and (s.isDeleted = false)
           """)
    Page<ReadSectionDTO> searchSections(@Param("key") String key, @Param("assoId") Long assoId, @Param("strId") Long strId, Pageable pageable);

    @Query("select (count(s.sectionId)>0) from Section s where trim(upper(s.sectionName)) = trim(upper(?1)) and s.association.assoId = ?2")
    boolean existsByNameAndAssoId(String sectionName, Long assoId);
}
