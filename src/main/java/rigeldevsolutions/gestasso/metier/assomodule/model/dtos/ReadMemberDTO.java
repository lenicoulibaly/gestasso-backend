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
    private String codeTypePiece;
    private String typePiece;
    private String numPiece;
    private String nomPere;
    private String nomMere;

    private Long adhesionId;
    private Long assoId;
    private String assoName;
    private String assoSigle;
    private Long sectionId;
    private String sectionName;
    private String sectionSigle;
    private String strName;
    private String strSigle;
    private boolean active;
}
