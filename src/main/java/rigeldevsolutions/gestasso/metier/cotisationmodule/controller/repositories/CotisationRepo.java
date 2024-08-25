package rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadAssociationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.ReadCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation;

public interface CotisationRepo extends JpaRepository<Cotisation, Long> {
    @Query("select (count(c.cotisationId)>0) from Cotisation c where trim(upper(c.nomCotisation)) = trim(upper(?1)) and c.section.sectionId = ?2")
    boolean existsByNameAndSectionId(String nomCotisation, Long sectionId);

    @Query("select (count(c.cotisationId)>0) from Cotisation c where trim(upper(c.nomCotisation)) = trim(upper(?1)) and c.association.assoId = ?2")
    boolean existsByNameAndAssoId(String nomCotisation, Long assoId);

    @Query("""
select (count(c.cotisationId)>0) from Cotisation  c left join c.association a left join c.section s where trim(upper(c.nomCotisation)) = trim(upper(?1)) 
 and c.cotisationId <> ?2 and 

 (exists (select c2 from Cotisation c2 left join c2.association a2 where trim(upper(c2.nomCotisation)) = trim(upper(?1)) and a2.assoId = a.assoId) or
 exists (select c3 from Cotisation c3 left join c3.section s3 where trim(upper(c3.nomCotisation)) = trim(upper(?1)) and s3.sectionId = s.sectionId))
""")
    boolean existsByNameAndCotisationId(String nomCotisation, Long cotisationId);

    @Query("""
    select new rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.ReadCotisationDTO(
    c.cotisationId, c.nomCotisation, c.montantCotisation, c.frequenceCotisation.name, c.frequenceCotisation.uniqueCode,
     c.modePrelevement.name, c.modePrelevement.uniqueCode, c.dateDebutCotisation, c.dateFinCotisation,c.delaiDeRigueurEnJours,
     a.assoName, a.sigle, a.assoId, s.sectionName, s.sigle, s.sectionId) 
    from Cotisation c left join c.association a left join c.section s where
    (
        case   
            when :actuel = true then current_date between coalesce(c.dateDebutCotisation, current_date) and coalesce(c.dateFinCotisation, current_date ) 
            else true   
        end
    )
    and 
    (locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(c.nomCotisation, '') ) as string))) >0 
           or locate(upper(coalesce(:key, '') ), upper(cast(c.montantCotisation as string))) =1
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(c.frequenceCotisation.name, '') ) as string))) >0
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce( c.frequenceCotisation.uniqueCode, '') ) as string))) >0
           
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(c.modePrelevement.name, '') ) as string))) >0
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(c.modePrelevement.uniqueCode, '') ) as string))) >0
           )
           and ((:assoId is null or :assoId = a.assoId) and (:sectionId is null or :sectionId = s.sectionId)) 
           and (c.isDeleted = false)
           
""")
    Page<ReadCotisationDTO> searchCotisations(@Param("key")String key, @Param("assoId")Long assoId, @Param("sectionId")Long sectionId, @Param("actuel") boolean actuel, Pageable pageable);
}