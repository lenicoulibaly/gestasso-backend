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
        Type paiement = typeRepo.save(new Type("paiements", TypeGroup.TYPE_REGLEMENT, "Paiement reçu", PersStatus.ACTIVE, null, null));
        Type reversement = typeRepo.save(new Type("reversements", TypeGroup.TYPE_REGLEMENT, "Reversement", PersStatus.ACTIVE, null, null));
        Type reglement_sinistre = typeRepo.save(new Type("REG-SIN", TypeGroup.TYPE_REGLEMENT, "Reglement Sinistre", PersStatus.ACTIVE, null, null));

        Type aLaSource = typeRepo.save(new Type("source", TypeGroup.MODE_PRELEVEMENT, "Prélèvement à la source", PersStatus.ACTIVE, null, null));
        Type spontane = typeRepo.save(new Type("spontane", TypeGroup.MODE_PRELEVEMENT, "Paiement spontané", PersStatus.ACTIVE, null, null));

        Type mensuel = typeRepo.save(new Type("mensuel", TypeGroup.TYPE_FREQUENCE, "Fréquence mensuelle", PersStatus.ACTIVE, null, null));
        Type trimestriel = typeRepo.save(new Type("trimestriel", TypeGroup.TYPE_FREQUENCE, "Fréquence trimestrielle", PersStatus.ACTIVE, null, null));
        Type semestriel = typeRepo.save(new Type("semestriel", TypeGroup.TYPE_FREQUENCE, "Fréquence semestrielle", PersStatus.ACTIVE, null, null));
        Type annuel = typeRepo.save(new Type("annuel", TypeGroup.TYPE_FREQUENCE, "Fréquence annuelle", PersStatus.ACTIVE, null, null));

        Type virement = typeRepo.save(new Type("VRG", TypeGroup.MODE_REGLEMENT, "Virement bancaire", PersStatus.ACTIVE, null, null));
        Type cheque = typeRepo.save(new Type("CHE", TypeGroup.MODE_REGLEMENT, "Chèque", PersStatus.ACTIVE, null, null));

        Type echeancierNaturel = typeRepo.save(new Type("ECH_NAT", TypeGroup.TYPE_ECHEANCIER, "Echéancier naturel", PersStatus.ACTIVE, null, null));
        Type echeancierProjet = typeRepo.save(new Type("ECH_PROJET", TypeGroup.TYPE_ECHEANCIER, "Echéancier de projet", PersStatus.ACTIVE, null, null));
        Type echeancierPersonnel = typeRepo.save(new Type("ECH_PERS", TypeGroup.TYPE_ECHEANCIER, "Echéancier personnel", PersStatus.ACTIVE, null, null));

// Doc user
        Type userDoc = typeRepo.save(new Type("DOC_USR", TypeGroup.DOCUMENT, "Document d'utilisateur", PersStatus.ACTIVE, null, "user"));
        Type photo = typeRepo.save(new Type("PHT", TypeGroup.DOCUMENT, "Photo", PersStatus.ACTIVE, null, "user"));
        typeParamRepo.save(new TypeParam(null, userDoc, photo, PersStatus.ACTIVE));

        // Doc règlement
        Type docReglement = typeRepo.save(new Type("DOC_REG", TypeGroup.DOCUMENT, "Document de règlement", PersStatus.ACTIVE, null, "reglement"));
        Type recuReglement = typeRepo.save(new Type("RECU_REG", TypeGroup.DOCUMENT, "Reçu de règlement", PersStatus.ACTIVE, null, "reglement"));
        Type chequeRegelemnt = typeRepo.save(new Type("CHEQ", TypeGroup.DOCUMENT, "Chèque de règlement", PersStatus.ACTIVE, null, "reglement"));
        Type bordereauVirement = typeRepo.save(new Type("BORD_VIR", TypeGroup.DOCUMENT, "Bordereau de virement", PersStatus.ACTIVE, null, "reglement"));
        Type ordreVirement = typeRepo.save(new Type("ORD_VIR", TypeGroup.DOCUMENT, "Ordre de virement", PersStatus.ACTIVE, null, "reglement"));
        typeParamRepo.save(new TypeParam(null, docReglement, recuReglement, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docReglement, chequeRegelemnt, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docReglement, bordereauVirement, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docReglement, ordreVirement, PersStatus.ACTIVE));

// Type de privilège
        Type t7 = typeRepo.save(new Type("PRV-AFF", TypeGroup.TYPE_PRV, "Privilège du module affaire", PersStatus.ACTIVE, null, null));
        Type t8 = typeRepo.save(new Type("PRV-ADM", TypeGroup.TYPE_PRV, "Privilège du module admin", PersStatus.ACTIVE, null, null));
        Type prvStatType = typeRepo.save(new Type("PRV-STAT", TypeGroup.TYPE_PRV, "Statistique", PersStatus.ACTIVE, null, null));
        Type prvUserType = typeRepo.save(new Type("PRV-USER", TypeGroup.TYPE_PRV, "Utilisateur", PersStatus.ACTIVE, null, null));
        Type prvFoncType = typeRepo.save(new Type("PRV-FONC", TypeGroup.TYPE_PRV, "Fonction", PersStatus.ACTIVE, null, null));
        Type prvPrvType = typeRepo.save(new Type("PRV-PRV", TypeGroup.TYPE_PRV, "Privilège", PersStatus.ACTIVE, null, null));
        Type prvRolType = typeRepo.save(new Type("PRV-ROL", TypeGroup.TYPE_PRV, "Rôle", PersStatus.ACTIVE, null, null));


        // Type fonction
        Type fonctionOperateurDeSaisieCedante = typeRepo.save(new Type("TYF_SAI_CED", TypeGroup.TYPE_FUNCTION, "Opérateur de saisie cédante", PersStatus.ACTIVE, null, null));
        Type fonctionSouscripteur = typeRepo.save(new Type("TYF_SOUS", TypeGroup.TYPE_FUNCTION, "Souscripteur", PersStatus.ACTIVE, null, null));

        Type fonctionValidateur = typeRepo.save(new Type("TYF_VAL", TypeGroup.TYPE_FUNCTION, "Validateur", PersStatus.ACTIVE, null, null));
        Type fonctionComptable = typeRepo.save(new Type("TYF_COMPTA", TypeGroup.TYPE_FUNCTION, "Comptable", PersStatus.ACTIVE, null, null));

        Type fonctionAdminFonc = typeRepo.save(new Type("TYF_ADM_FONC", TypeGroup.TYPE_FUNCTION, "Administrateur fonctionnel", PersStatus.ACTIVE, null, null));
        Type fonctionAdminTech = typeRepo.save(new Type("TYF_ADM_TECH", TypeGroup.TYPE_FUNCTION, "Administrateur technique", PersStatus.ACTIVE, null, null));

        Type fonctionDev = typeRepo.save(new Type("TYF_DEV", TypeGroup.TYPE_FUNCTION, "Développeur", PersStatus.ACTIVE, null, null));

    }
}
