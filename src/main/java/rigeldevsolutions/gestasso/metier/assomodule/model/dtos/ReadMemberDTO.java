package rigeldevsolutions.gestasso.metier.assomodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadMemberDTO
{
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String tel;
    private String lieuNaissance;
    private LocalDate dateNaissance;
    private String civilite;
    private String paysCode;
    private String nationalite;
    private String nomPere;
    private String nomMere;
    private boolean active;
}
