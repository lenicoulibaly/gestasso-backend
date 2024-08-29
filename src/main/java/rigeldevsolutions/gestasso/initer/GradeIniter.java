package rigeldevsolutions.gestasso.initer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.grademodule.controller.repositories.GradeRepo;
import rigeldevsolutions.gestasso.grademodule.model.dtos.ReadGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.entities.Grade;
import rigeldevsolutions.gestasso.grademodule.model.enums.Categorie;

@Component @RequiredArgsConstructor
public class GradeIniter implements Initer
{
    private final GradeRepo gradeRepo;
    @Override
    public void init() {
        Grade a3 = gradeRepo.save(new Grade("A3", "A3", 43, Categorie.A));
        Grade a4 = gradeRepo.save(new Grade("A4", "A4", 44, Categorie.A));
        Grade a5 = gradeRepo.save(new Grade("A5", "A5", 45, Categorie.A));
        Grade a6 = gradeRepo.save(new Grade("A6", "A6", 46, Categorie.A));
        Grade a7 = gradeRepo.save(new Grade("A7", "A7", 47, Categorie.A));
        Grade b1 = gradeRepo.save(new Grade("B1", "B1", 31, Categorie.B));
        Grade b2 = gradeRepo.save(new Grade("B2", "B2", 32, Categorie.B));
        Grade b3 = gradeRepo.save(new Grade("B3", "B3", 33, Categorie.B));
        Grade c1 = gradeRepo.save(new Grade("C1", "C1", 21, Categorie.C));
        Grade c2 = gradeRepo.save(new Grade("C2", "C2", 22, Categorie.C));
        Grade c3 = gradeRepo.save(new Grade("C3", "C3", 23, Categorie.C));
        Grade d1 = gradeRepo.save(new Grade("D1", "D1", 11, Categorie.D));
        Grade d2 = gradeRepo.save(new Grade("D2", "D2", 12, Categorie.D));
        Grade d3 = gradeRepo.save(new Grade("D3", "D3", 13, Categorie.D));
    }
}
