package rigeldevsolutions.gestasso.grademodule.controller.service;

import rigeldevsolutions.gestasso.grademodule.model.dtos.CreateGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.dtos.ReadGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.dtos.UpdateGradeDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IGradeService
{
    List<ReadGradeDTO> getAllGrades();
    ReadGradeDTO getGradesById(Long grdId);
    List<ReadGradeDTO> getGradesByCategoryName(String catName);

    Page<ReadGradeDTO> searchPageOfGrades(String key, int pageNum, int pageSize);
    Page<ReadGradeDTO> searchPageOfDeletedGrades(String key, int pageNum, int pageSize);

    ReadGradeDTO createGrade(CreateGradeDTO dto);
    ReadGradeDTO updateGrade(UpdateGradeDTO dto);
}
