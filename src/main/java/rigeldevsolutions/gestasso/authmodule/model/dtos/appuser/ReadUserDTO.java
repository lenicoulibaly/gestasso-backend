package rigeldevsolutions.gestasso.authmodule.model.dtos.appuser;

import rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction.ReadFncDTO;
import rigeldevsolutions.gestasso.authmodule.model.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadUserDTO
{
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String tel;
    private String lieuNaissance;
    private LocalDate dateNaissance;
    private Long assoId;
    private String assoName;
    private Long sectionId;
    private String sectionName;
    private String civilite;
    private String paysCode;
    private String nationalite;
    private String codeTypePiece;
    private String typePiece;
    private String numPiece;
    private String nomPere;
    private String nomMere;

    private String codeTypeUtilisateur;
    private String typeUtilisateur;
    private boolean active;

    private boolean notBlocked;
    private Long currentFunctionId;
    private String codeStatut;
    private String statut;

    private UserStatus status;
    private ReadFncDTO currentFnc;

    public ReadUserDTO(Long userId, String firstName, String lastName,
                       String email, String tel, String civilite,
                       String typeUtilisateur, boolean active, boolean notBlocked, String statut)
    {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tel = tel;
        this.civilite = civilite;
        this.typeUtilisateur = typeUtilisateur;
        this.active = active;
        this.notBlocked = notBlocked;
        this.statut = statut;

    }

    public ReadUserDTO(Long userId, String firstName, String lastName, String email, String tel, String lieuNaissance,
                       Long assoId, String assoName, Long sectionId, String sectionName, LocalDate dateNaissance, Long idEcole, String nomEcole, String sigleEcole, String civilite, String paysCode, String nationalite, String codeTypePiece, String typePiece, String numPiece, String nomPere, String nomMere, String codeTypeUtilisateur, String typeUtilisateur, boolean active, boolean notBlocked, Long currentFunctionId, String codeStatut, String statut) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tel = tel;
        this.lieuNaissance = lieuNaissance;
        this.dateNaissance = dateNaissance;
        this.assoId = assoId;
        this.assoName = assoName;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.civilite = civilite;
        this.paysCode = paysCode;
        this.nationalite = nationalite;
        this.codeTypePiece = codeTypePiece;
        this.typePiece = typePiece;
        this.numPiece = numPiece;
        this.nomPere = nomPere;
        this.nomMere = nomMere;
        this.codeTypeUtilisateur = codeTypeUtilisateur;
        this.typeUtilisateur = typeUtilisateur;
        this.active = active;
        this.notBlocked = notBlocked;
        this.currentFunctionId = currentFunctionId;
        this.codeStatut = codeStatut;
        this.statut = statut;
    }
}
