package rigeldevsolutions.gestasso.archivemodule.controller.repositories;

import rigeldevsolutions.gestasso.archivemodule.model.dtos.response.ReadDocDTO;
import rigeldevsolutions.gestasso.archivemodule.model.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long>
{
    @Query("select d.docPath from Document d where d.docId = ?1")
    String getDocumentPath(Long docId);

    @Query("""
        select new rigeldevsolutions.gestasso.archivemodule.model.dtos.response.ReadDocDTO(
        d.docId, d.docNum, d.docName, d.docDescription, d.docPath, d.docType.uniqueCode, d.docType.name
        ) 
        from Document d left join d.user u left join d.association a left join d.section s
        where (
        locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(d.docDescription, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(d.docType.name, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(d.docNum, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(d.docName, '') ) as string))) >0
        )
        and
        (u.userId is null or u.userId = :userId)
        and (a.assoId is null or a.assoId = :assoId)
        and (s.sectionId is null or s.sectionId = :sectionId)
    """)
    Page<ReadDocDTO> getAllDocsForObject(@Param("userId") Long userId,
                                         @Param("assoId") Long assoId,
                                         @Param("sectionId") Long sectionId,  @Param("key") String key, Pageable pageable);
}