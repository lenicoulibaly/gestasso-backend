package rigeldevsolutions.gestasso.metier.assomodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadMemberDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;

public interface AdhesionRepo extends JpaRepository<Adhesion, Long>
{
    @Query("""
        select new rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadMemberDTO(
        u.userId, u.firstName, u.lastName, u.email, u.tel, u.lieuNaissance, u.dateNaissance, u.civilite.uniqueCode, n.codePays, n.nationalite, u.nomPere, u.nomMere, a.active) 
        from Adhesion a left join a.member u left join a.section sect left join sect.association sectAsso 
        left join a.association asso left join sect.strTutelle str
        left join u.nationalite n
        where (locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(u.firstName, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.lastName, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.email, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.tel, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.lieuNaissance, '') ) as string) )) >0)
        and (:assoId is null or asso.assoId = :assoId or sectAsso.assoId = :assoId)
        and (:sectionId is null or sect.sectionId = :sectionId)
""")
    Page<ReadMemberDTO> searchMembers(@Param("key") String key,
                                      @Param("assoId") Long assoId,
                                      @Param("sectionId") Long sectionId,
                                      Pageable pageable);
}