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

import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/grades")
public class GradeController
{
    private final IGradeService gradeService;

    @GetMapping(path = "/all")
    public List<ReadGradeDTO> gotoGradeList(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize)
    {
        return gradeService.getAllGrades();
    }
}
