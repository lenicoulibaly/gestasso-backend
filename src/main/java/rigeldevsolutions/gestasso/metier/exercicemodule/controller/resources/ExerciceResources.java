package rigeldevsolutions.gestasso.metier.exercicemodule.controller.resources;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rigeldevsolutions.gestasso.metier.exercicemodule.controller.services.IExercieService;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.CreateExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.ReadExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.UpdateExerciceDTO;

import java.util.List;

@RestController @ResponseStatus(HttpStatus.OK)
@RequestMapping("/exercices")
@RequiredArgsConstructor
public class ExerciceResources
{
    private final IExercieService exoService;

    @GetMapping(path = "/list")
    public List<ReadExerciceDTO> searchExercice(@RequestParam(defaultValue = "") String key)
    {
        return exoService.searchExercice(key);
    }

    @PostMapping(path = "/create")
    public ReadExerciceDTO createExercice(@RequestBody @Valid CreateExerciceDTO dto)
    {
        return exoService.createExercice(dto);
    }

    @PutMapping(path = "/update")
    public ReadExerciceDTO updateExercice(@RequestBody @Valid UpdateExerciceDTO dto)
    {
        return exoService.updateExercice(dto);
    }

    @PutMapping(path = "/activate/{exeCode}")
    public ReadExerciceDTO activateExercice(@PathVariable Long exeCode)
    {
        return exoService.activateExercice(exeCode);
    }

    @GetMapping(path = "/getCourantAndPlus1")
    public List<ReadExerciceDTO> getCourantAndPlus1(){
        return exoService.getExerciceCourantAndPlus1();
    }
}