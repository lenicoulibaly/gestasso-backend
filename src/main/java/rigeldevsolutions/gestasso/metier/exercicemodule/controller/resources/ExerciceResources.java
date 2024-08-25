package rigeldevsolutions.gestasso.metier.exercicemodule.controller.resources;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IActionIdentifierService;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.exercicemodule.controller.services.IExercieService;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.constants.ExerciceActions;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.CreateExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.ReadExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.UpdateExerciceDTO;

import java.util.List;

@RestController @ResponseStatus(HttpStatus.OK)
@RequestMapping("/exercices")
@RequiredArgsConstructor
public class ExerciceResources {
    private final IExercieService exoService;
    private final IActionIdentifierService ais;

    @GetMapping(path = "/list")
    public List<ReadExerciceDTO> searchExercice(@RequestParam(defaultValue = "") String key)
    {
        return exoService.searchExercice(key);
    }

    @PostMapping(path = "/create")
    public ReadExerciceDTO createExercice(@RequestBody @Valid CreateExerciceDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(ExerciceActions.CREATE_EXERCICE);
        return exoService.createExercice(dto, ai);
    }

    @PutMapping(path = "/update")
    public ReadExerciceDTO updateExercice(@RequestBody @Valid UpdateExerciceDTO dto)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(ExerciceActions.UPDATE_EXERCICE);
        return exoService.updateExercice(dto, ai);
    }

    @PutMapping(path = "/activate/{exeCode}")
    public ReadExerciceDTO activateExercice(@PathVariable Long exeCode)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(ExerciceActions.ACTIVATE_EXERCICE);
        return exoService.activateExercice(exeCode, ai);
    }

    @GetMapping(path = "/getCourantAndPlus1")
    public List<ReadExerciceDTO> getCourantAndPlus1(){
        return exoService.getExerciceCourantAndPlus1();
    }
}