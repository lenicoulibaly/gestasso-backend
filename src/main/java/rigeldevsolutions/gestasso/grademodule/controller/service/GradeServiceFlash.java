package rigeldevsolutions.gestasso.grademodule.controller.service;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import rigeldevsolutions.gestasso.grademodule.model.dtos.CreateGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.dtos.ReadGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.dtos.UpdateGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.enums.Categorie;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("gradeService") //@Profile({"flash"})
public class GradeServiceFlash implements IGradeService
{
    @Override
    public List<ReadGradeDTO> getAllGrades()
    {
        ReadGradeDTO g1 = ReadGradeDTO.builder().nomGrade("A3").categorie(Categorie.A.name()).build();
        ReadGradeDTO g2 = ReadGradeDTO.builder().nomGrade("A4").categorie(Categorie.A.name()).build();
        ReadGradeDTO g3 = ReadGradeDTO.builder().nomGrade("A5").categorie(Categorie.A.name()).build();
        ReadGradeDTO g4 = ReadGradeDTO.builder().nomGrade("A6").categorie(Categorie.A.name()).build();
        ReadGradeDTO g5 = ReadGradeDTO.builder().nomGrade("A7").categorie(Categorie.A.name()).build();
        ReadGradeDTO g6 = ReadGradeDTO.builder().nomGrade("B1").categorie(Categorie.B.name()).build();
        ReadGradeDTO g7 = ReadGradeDTO.builder().nomGrade("B2").categorie(Categorie.B.name()).build();
        ReadGradeDTO g8 = ReadGradeDTO.builder().nomGrade("B3").categorie(Categorie.B.name()).build();
        ReadGradeDTO g9 = ReadGradeDTO.builder().nomGrade("C1").categorie(Categorie.C.name()).build();
        ReadGradeDTO g10 = ReadGradeDTO.builder().nomGrade("C2").categorie(Categorie.C.name()).build();
        ReadGradeDTO g11 = ReadGradeDTO.builder().nomGrade("C3").categorie(Categorie.C.name()).build();
        ReadGradeDTO g12 = ReadGradeDTO.builder().nomGrade("D1").categorie(Categorie.D.name()).build();
        ReadGradeDTO g13 = ReadGradeDTO.builder().nomGrade("D2").categorie(Categorie.D.name()).build();
        ReadGradeDTO g14 = ReadGradeDTO.builder().nomGrade("D3").categorie(Categorie.D.name()).build();
        return Arrays.asList(g1, g2, g3,g4, g5, g6,g7, g8, g9, g10, g11,g12, g13, g14);
    }

    @Override
    public ReadGradeDTO getGradeByGradeCode(String gradeCode)
    {
        return getAllGrades().stream().filter(grd->grd.getGradeCode().equals(gradeCode)).findFirst().orElse(null);
    }

    @Override
    public List<ReadGradeDTO> getGradesByCategoryName(String catName)
    {
        Categorie categorie = EnumUtils.getEnum(Categorie.class, catName);
        List<ReadGradeDTO> grades = getAllGrades().stream().filter(c->c.getCategorie().equals(catName)).collect(Collectors.toList());
        return grades;
    }
}
