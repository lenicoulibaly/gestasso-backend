package rigeldevsolutions.gestasso.grademodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadGradeDTO
{
    private Long idGrade;
    private String nomGrade;
    private int rang; //D = 1 , C = 2 ,  B = 3 , A = 4
    private String categorie;
    private String status;
    private long nbrAgents;
    private double proportion;
}
