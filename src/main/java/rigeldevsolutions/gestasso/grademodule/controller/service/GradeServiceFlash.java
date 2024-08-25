package rigeldevsolutions.gestasso.grademodule.controller.service;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import rigeldevsolutions.gestasso.grademodule.model.dtos.CreateGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.dtos.ReadGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.dtos.UpdateGradeDTO;

import java.util.Arrays;
import java.util.List;

@Service("gradeService") @Profile({"deprecated"}) //@Profile({"flash"})
public class GradeServiceFlash implements IGradeService
{
    @Override
    public List<ReadGradeDTO> getAllGrades()
    {
        ReadGradeDTO g1 = ReadGradeDTO.builder().nomGrade("A3").gradeId(1L).build();
        ReadGradeDTO g2 = ReadGradeDTO.builder().nomGrade("A4").gradeId(2L).build();
        ReadGradeDTO g3 = ReadGradeDTO.builder().nomGrade("A5").gradeId(3L).build();
        ReadGradeDTO g4 = ReadGradeDTO.builder().nomGrade("A6").gradeId(4L).build();
        ReadGradeDTO g5 = ReadGradeDTO.builder().nomGrade("A7").gradeId(5L).build();
        ReadGradeDTO g6 = ReadGradeDTO.builder().nomGrade("B2").gradeId(1L).build();
        ReadGradeDTO g7 = ReadGradeDTO.builder().nomGrade("B3").gradeId(2L).build();
        ReadGradeDTO g8 = ReadGradeDTO.builder().nomGrade("C2").gradeId(3L).build();
        ReadGradeDTO g9 = ReadGradeDTO.builder().nomGrade("D2").gradeId(4L).build();
        return Arrays.asList(g1, g2, g3,g4, g5, g6,g7, g8, g9);
    }

    @Override
    public ReadGradeDTO getGradesById(Long grdId) {
        return getAllGrades().stream().filter(grd->grd.getGradeId().equals(grdId)).findFirst().orElse(null);
    }

    @Override
    public List<ReadGradeDTO> getGradesByCategoryName(String catName) {
        return null;
    }


    @Override
    public Page<ReadGradeDTO> searchPageOfGrades(String key, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public Page<ReadGradeDTO> searchPageOfDeletedGrades(String key, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public ReadGradeDTO createGrade(CreateGradeDTO dto) {
        return null;
    }

    @Override
    public ReadGradeDTO updateGrade(UpdateGradeDTO dto) {
        return null;
    }
}
