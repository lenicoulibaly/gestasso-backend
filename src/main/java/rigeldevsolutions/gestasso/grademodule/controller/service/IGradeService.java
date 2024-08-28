package rigeldevsolutions.gestasso.grademodule.controller.service;

import rigeldevsolutions.gestasso.grademodule.model.dtos.CreateGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.dtos.ReadGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.dtos.UpdateGradeDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IGradeService
{
    List<ReadGradeDTO> getAllGrades();
    ReadGradeDTO getGradeByGradeCode(String gradeCode);
    List<ReadGradeDTO> getGradesByCategoryName(String catName);
}
