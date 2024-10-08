package rigeldevsolutions.gestasso.grademodule.model.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@UniqueGrade
public class UpdateGradeDTO
{
    @ExistingGradeCode
    private String gradeCode;
    @Min(value = 1, message = "Le rang doit être compris entre 1 et 7")
    @Max(value = 7, message = "Le rang doit être compris entre 1 et 7")
    private int rang; //D = 1 , C = 2 ,  B = 3 , A = 4
    @ExistingCategoryName
    private String categorie;
    private String nomGrade;

    public UpdateGradeDTO(String codeGrade, int rang, String categorie) {
        this.gradeCode = codeGrade;
        this.rang = rang;
        this.categorie = categorie;
        this.nomGrade = categorie+rang;
    }
}
