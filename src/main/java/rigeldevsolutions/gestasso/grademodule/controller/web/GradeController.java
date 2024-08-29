package rigeldevsolutions.gestasso.grademodule.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rigeldevsolutions.gestasso.grademodule.controller.service.IGradeService;
import rigeldevsolutions.gestasso.grademodule.model.dtos.ReadGradeDTO;

import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/grades")
public class GradeController
{
    private final IGradeService gradeService;

    @GetMapping(path = "/all")
    public List<ReadGradeDTO> gotoGradeList()
    {
        return gradeService.getAllGrades();
    }
}
