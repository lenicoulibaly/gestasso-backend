package rigeldevsolutions.gestasso.metier.assomodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateMembreDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadMembreDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;
import rigeldevsolutions.gestasso.sharedmodule.dtos.SelectOption;

import java.util.List;
import java.util.Optional;

public interface MembreRepo extends JpaRepository<Adhesion, Long>
{
    @Query("""
        select new rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadMembreDTO(
        a.adhesionId, u.userId, u.firstName, u.lastName, u.matriculeFonctionnaire, u.email, u.tel, 
        u.lieuNaissance, u.dateNaissance, u.civilite.uniqueCode, u.civilite.name, n.codePays, n.nationalite, u.nomPere, 
        u.nomMere, a.active, u.notBlocked, sect.sectionId, sect.sectionName, asso.assoId, asso.assoName) 
        from Adhesion a left join a.member u left join a.section sect left join sect.association sectAsso 
        left join a.association asso left join sect.strTutelle str
        left join u.nationalite n
        where (locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(u.firstName, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.lastName, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.email, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.tel, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(u.lieuNaissance, '') ) as string) )) >0)
        and ((asso.assoId = :assoId or sectAsso.assoId = :assoId) or (sect.sectionId = :sectionId))
""")
    Page<ReadMembreDTO>

    searchMembers(@Param("key") String key,
                                      @Param("assoId") Long assoId,
                                      @Param("sectionId") Long sectionId,
                                      Pageable pageable);

    @Query("""
    select new rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateMembreDTO
    (u.userId, u.matriculeFonctionnaire, u.email, u.tel, u.firstName, u.lastName, u.lieuNaissance, u.dateNaissance, u.civilite.uniqueCode, u.civilite.name)
    from AppUser u where u.matriculeFonctionnaire = ?1 or u.email = ?1 or u.tel = ?1
""")
    CreateMembreDTO findByIdIdentifiant(String uniqueIdentifier);

    @Query("""
    select new rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateMembreDTO
    (u.userId, u.matriculeFonctionnaire, u.email, u.tel, u.firstName, u.lastName, u.lieuNaissance, u.dateNaissance, u.civilite.uniqueCode, u.civilite.name)
    from AppUser u where u.matriculeFonctionnaire = ?1
""")
    CreateMembreDTO findByMatricule(String matricule);

    @Query("""
    select new rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateMembreDTO
    (u.userId, u.matriculeFonctionnaire, u.email, u.tel, u.firstName, u.lastName, u.lieuNaissance, u.dateNaissance, u.civilite.uniqueCode, u.civilite.name)
    from AppUser u where u.email = ?1
""")
    CreateMembreDTO findByEmail(String email);

    @Query("""
    select new rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateMembreDTO
    (u.userId, u.matriculeFonctionnaire, u.email, u.tel, u.firstName, u.lastName, u.lieuNaissance, u.dateNaissance, u.civilite.uniqueCode, u.civilite.name)
    from AppUser u where u.tel = ?1
""")
    CreateMembreDTO findByTel(String tel);

    @Query("select a from Adhesion a where a.member.email = ?1 and a.section.sectionId = ?2")
    Optional<Adhesion> findByEmailAndSection(String email, Long sectionId);

    @Query("select a from Adhesion a where a.member.email = ?1 and a.association.assoId = ?2")
    Optional<Adhesion> findByEmailAndAsso(String email, Long assoId);

    @Query("""
    select new rigeldevsolutions.gestasso.sharedmodule.dtos.SelectOption(a.adhesionId, 
    concat(m.firstName, ' ', m.lastName, ' (', coalesce('', m.matriculeFonctionnaire), '-', m.email, '-', m.tel, ')' ) ) 
    from Adhesion a join a.member m where a.association.assoId = ?1
    """)
    List<SelectOption> getAdhesionOptions(Long assoId);
}