package rigeldevsolutions.gestasso.grademodule.controller.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rigeldevsolutions.gestasso.grademodule.controller.service.IGradeService;
import rigeldevsolutions.gestasso.grademodule.model.dtos.CreateGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.dtos.ReadGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.dtos.UpdateGradeDTO;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/grades")
public class GradeController
{
    private final IGradeService gradeService;

    @GetMapping(path = "/search")
    public Page<ReadGradeDTO> gotoGradeList(Model model, @RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize)
    {
        key = key==null || key.trim().equals("")? "" :key;
        Page<ReadGradeDTO> grades = gradeService.searchPageOfGrades(key, pageNum, pageSize);
        return grades;
    }

    //==================================

    @PostMapping(path = "/create")
    public ReadGradeDTO createGrade(Model model, @Valid CreateGradeDTO dto)
    {
        ReadGradeDTO grade = gradeService.createGrade(dto);
        return grade;
    }

    @PostMapping(path = "/sigrh/administration/grades/update")
    public ReadGradeDTO updateGrade(Model model, @Valid UpdateGradeDTO dto)
    {
        ReadGradeDTO grade = gradeService.updateGrade(dto);
        return grade;
    }
}
