package rigeldevsolutions.gestasso.initer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rigeldevsolutions.gestasso.sharedmodule.enums.PersStatus;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeParamRepo;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeRepo;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;
import rigeldevsolutions.gestasso.typemodule.model.entities.TypeParam;
import rigeldevsolutions.gestasso.typemodule.model.enums.TypeGroup;

@Service @RequiredArgsConstructor
public class TypeIniter implements Initer
{
    private final TypeRepo typeRepo;
    private final TypeParamRepo typeParamRepo;
    @Override
    public void init()
    {
        Type paiementCotisation = typeRepo.save(new Type("PAIE-COT", TypeGroup.TYPE_PAIEMENT, "Paiement cotisation",0, PersStatus.ACTIVE, null, null));
        Type paiementPretScolaire = typeRepo.save(new Type("PAIE-PS", TypeGroup.TYPE_PAIEMENT, "Paiement de prêt scolaire",0, PersStatus.ACTIVE, null, null));
        Type paiementAcquisitionTerrain = typeRepo.save(new Type("PAIE-TER", TypeGroup.TYPE_PAIEMENT, "Paiement pour acquisition de terrain",0, PersStatus.ACTIVE, null, null));
        Type paiementAcquisitionLogement = typeRepo.save(new Type("PAIE-LOG", TypeGroup.TYPE_PAIEMENT, "Paiement pour acquisition de logement",0, PersStatus.ACTIVE, null, null));

        Type aLaSource = typeRepo.save(new Type("SOURCE", TypeGroup.MODE_PRELEVEMENT, "Prélèvement à la source", 0, PersStatus.ACTIVE, null, null));
        Type spontane = typeRepo.save(new Type("SPONT", TypeGroup.MODE_PRELEVEMENT, "Paiement spontané", 0, PersStatus.ACTIVE, null, null));

        Type mensuel = typeRepo.save(new Type("MENSUEL", TypeGroup.TYPE_FREQUENCE, "Mensuel", 0, PersStatus.ACTIVE, null, null));
        Type trimestriel = typeRepo.save(new Type("TRIMESTRIEL", TypeGroup.TYPE_FREQUENCE, "Trimestriel", 0, PersStatus.ACTIVE, null, null));
        Type semestriel = typeRepo.save(new Type("SEMESTRIEL", TypeGroup.TYPE_FREQUENCE, "Semestriel", 0, PersStatus.ACTIVE, null, null));
        Type annuel = typeRepo.save(new Type("ANNUEL", TypeGroup.TYPE_FREQUENCE, "Annuel", 0, PersStatus.ACTIVE, null, null));

        Type virement = typeRepo.save(new Type("VRB", TypeGroup.MODE_PAIEMENT, "Virement bancaire", 0, PersStatus.ACTIVE, null, null));
        Type cheque = typeRepo.save(new Type("CHEQ", TypeGroup.MODE_PAIEMENT, "Chèque", 0, PersStatus.ACTIVE, null, null));
        Type espece = typeRepo.save(new Type("ESPECE", TypeGroup.MODE_PAIEMENT, "Espèce", 0, PersStatus.ACTIVE, null, null));


        Type echeancierNaturel = typeRepo.save(new Type("ECH-NAT", TypeGroup.TYPE_ECHEANCIER, "Echéancier naturel", 0, PersStatus.ACTIVE, null, null));
        Type echeancierProjet = typeRepo.save(new Type("ECH-PROJET", TypeGroup.TYPE_ECHEANCIER, "Echéancier de projet", 0, PersStatus.ACTIVE, null, null));
        Type echeancierPersonnel = typeRepo.save(new Type("ECH-PERS", TypeGroup.TYPE_ECHEANCIER, "Echéancier personnel", 0, PersStatus.ACTIVE, null, null));

// Doc user
        Type userDoc = typeRepo.save(new Type("DOC-USR", TypeGroup.DOCUMENT, "Document d'utilisateur", 0, PersStatus.ACTIVE, null, "user"));
        Type photo = typeRepo.save(new Type("PHT", TypeGroup.DOCUMENT, "Photo", 0, PersStatus.ACTIVE, null, "user"));
        typeParamRepo.save(new TypeParam(null, userDoc, photo, PersStatus.ACTIVE));

        // Doc règlement
        Type docReglement = typeRepo.save(new Type("DOC-PAIE", TypeGroup.DOCUMENT, "Document de règlement", 0, PersStatus.ACTIVE, null, "reglement"));
        Type recuReglement = typeRepo.save(new Type("RECU-PAIE", TypeGroup.DOCUMENT, "Reçu de règlement", 0, PersStatus.ACTIVE, null, "reglement"));
        Type chequeRegelemnt = typeRepo.save(new Type("DOC-CHEQ", TypeGroup.DOCUMENT, "Chèque de règlement", 0, PersStatus.ACTIVE, null, "reglement"));

        typeParamRepo.save(new TypeParam(null, docReglement, recuReglement, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docReglement, chequeRegelemnt, PersStatus.ACTIVE));

// Type de privilège
        Type t7 = typeRepo.save(new Type("PRV-AFF", TypeGroup.TYPE_PRV, "Privilège du module affaire", 0, PersStatus.ACTIVE, null, null));
        Type t8 = typeRepo.save(new Type("PRV-ADM", TypeGroup.TYPE_PRV, "Privilège du module admin", 0, PersStatus.ACTIVE, null, null));
        Type prvStatType = typeRepo.save(new Type("PRV-STAT", TypeGroup.TYPE_PRV, "Statistique", 0, PersStatus.ACTIVE, null, null));
        Type prvUserType = typeRepo.save(new Type("PRV-USER", TypeGroup.TYPE_PRV, "Utilisateur", 0, PersStatus.ACTIVE, null, null));
        Type prvFoncType = typeRepo.save(new Type("PRV-FONC", TypeGroup.TYPE_PRV, "Fonction", 0, PersStatus.ACTIVE, null, null));
        Type prvPrvType = typeRepo.save(new Type("PRV-PRV", TypeGroup.TYPE_PRV, "Privilège", 0, PersStatus.ACTIVE, null, null));
        Type prvRolType = typeRepo.save(new Type("PRV-ROL", TypeGroup.TYPE_PRV, "Rôle", 0, PersStatus.ACTIVE, null, null));


        // Type fonction

        Type fonctionAdminFonc = typeRepo.save(new Type("TYF-ADM-FONC", TypeGroup.TYPE_FUNCTION, "Administrateur fonctionnel", 0, PersStatus.ACTIVE, null, null));
        Type fonctionAdminTech = typeRepo.save(new Type("TYF-ADM-TECH", TypeGroup.TYPE_FUNCTION, "Administrateur technique", 0, PersStatus.ACTIVE, null, null));

        Type fonctionDev = typeRepo.save(new Type("TYF-DEV", TypeGroup.TYPE_FUNCTION, "Développeur", 0, PersStatus.ACTIVE, null, null));
        Type membreAssociation = typeRepo.save(new Type("TYF-MBR-ASSO", TypeGroup.TYPE_FUNCTION, "Membre d'association", 0, PersStatus.ACTIVE, null, null));
        Type membreSection = typeRepo.save(new Type("TYF-MBR-SECT", TypeGroup.TYPE_FUNCTION, "Membre de section", 0, PersStatus.ACTIVE, null, null));

        Type monsieur = typeRepo.save(new Type("M.", TypeGroup.TYPE_CIVILITE, "Monsieur", 0, PersStatus.ACTIVE, null, null));
        Type mme = typeRepo.save(new Type("MME", TypeGroup.TYPE_CIVILITE, "Madame", 0, PersStatus.ACTIVE, null, null));
        Type mlle = typeRepo.save(new Type("MLLE", TypeGroup.TYPE_CIVILITE, "Mademoiselle", 0, PersStatus.ACTIVE, null, null));

    }
}
