package rigeldevsolutions.gestasso.metier.assomodule.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import rigeldevsolutions.gestasso.TestDatabaseConfig;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IJwtService;
import rigeldevsolutions.gestasso.metier.assomodule.controller.repositories.AssoRepo;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.enums.Frequence;
import rigeldevsolutions.gestasso.sharedmodule.enums.PersStatus;
import rigeldevsolutions.gestasso.sharedmodule.utilities.StringUtils;
import rigeldevsolutions.gestasso.structuremodule.controller.repositories.StrRepo;
import rigeldevsolutions.gestasso.structuremodule.model.entities.Structure;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeRepo;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;
import rigeldevsolutions.gestasso.typemodule.model.enums.TypeGroup;

import java.math.BigDecimal;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestDatabaseConfig.class)
public class AssoRepoTest
{
    @Autowired private AssoRepo assoRepo;
    @Autowired private StrRepo strRepo;
    @Autowired private TypeRepo typeRepo;

    @Test
    void test_searchAssociations()
    {
        Type MIN = typeRepo.save(new Type("MIN", TypeGroup.TYPE_STR, "Ministère", PersStatus.ACTIVE, null, null));
        Type DG = typeRepo.save(new Type("DG", TypeGroup.TYPE_STR, "Direction Générale", PersStatus.ACTIVE, null, null));
        Type DC = typeRepo.save(new Type("DC", TypeGroup.TYPE_STR, "Direction Centrale", PersStatus.ACTIVE, null, null));

        Structure mfb = strRepo.save(new Structure(null, "MFB", "Ministère des Finances et du Budget", 1l, "MFB", null, MIN, "", "", "", "", null, null, null, PersStatus.ACTIVE, null));
        Structure mpe = strRepo.save(new Structure(null, "MPE", "Ministère du Patrimoine et de l'Economie", 1l, "MPE", null, MIN, "", "", "", "", null, null, null, PersStatus.ACTIVE, null));

        Structure dgmp = strRepo.save(new Structure(null, "MFB/DGMP", "Direction Générale des Marchés Publics", 2l, "DGMP", mfb, DG, "", "", "", "", null, null, null, PersStatus.ACTIVE, null));
        Structure dgbf = strRepo.save(new Structure(null, "MFB/DGBF", "Direction Générale du Budget et des Finances", 2l, "DGBF", mfb, DG, "", "", "", "", null, null, null, PersStatus.ACTIVE, null));
        Structure dgi = strRepo.save(new Structure(null, "MFB/DGI", "Direction Générale des Impôts", 2l, "DGI", mfb, DG, "", "", "", "", null, null, null, PersStatus.ACTIVE, null));
        Structure douane = strRepo.save(new Structure(null, "MFB/DGD", "Direction Générale des Douanes", 2l, "DGD", mfb, DG, "", "", "", "", null, null, null, PersStatus.ACTIVE, null));
        Structure tresor = strRepo.save(new Structure(null, "MPE/DGTCP", "Direction Générale du Trésor et de la Comptabilité Publics", 2l, "DGTCP", mpe, DG, "", "", "", "", null, null, null, PersStatus.ACTIVE, null));
        Structure solde = strRepo.save(new Structure(null, "MFB/DGBF/DS", "Direction de la Solde", 3l, "DS", dgbf, DC, "", "", "", "", null, null, null, PersStatus.ACTIVE, null));

        Association mafib = assoRepo.save(new Association(null, "Mutuelle des Agents des Finances Générales", "Plateau", "MAFIB", new BigDecimal(5000)));
        Association synafig = assoRepo.save(new Association(null, "Syndicat des Agents des Finances Générales", "Plateau", "SYNAFIG", new BigDecimal(5000)));
        Association resafig = assoRepo.save(new Association(null, "Réseau des Agents des Finances Générales", "Plateau", "RESAFIG", new BigDecimal(5000)));
        Association alternative = assoRepo.save(new Association(null, "Alternative Syndicale des Finances Générales", "Plateau", "ALTER", new BigDecimal(5000)));
        Association fondDgmp = assoRepo.save(new Association(null, "Fonds de solidarité de la DGMP", "Riviera Bonoumin", "FDGMP", null));
        Association madgi = assoRepo.save(new Association(null, "Mutuelle des Agents de la DGI", "Plateau", "MADGI", new BigDecimal(20000)));
        Association madgbf = assoRepo.save(new Association(null, "Mutuelle des Agents de la DGBF", "Plateau", "MADGBF", new BigDecimal(10000)));
        Association fondDouane = assoRepo.save(new Association(null, "Fonds de Solidarité de la Douane", "Plateau", "FSD", new BigDecimal(5000)));
        Association fondSolde = assoRepo.save(new Association(null, "Fonds de Solidarité de la Solde", "Plateau", "FSS", new BigDecimal(10000)));
        Association monTresor = assoRepo.save(new Association(null, "Mutuelle des Agents du Trésor", "Plateau", "MONTRESOR", new BigDecimal(5000)));

        Page<ReadAssociationDTO> associations = assoRepo.searchAssociations("",  PageRequest.of(0, 5));

        Assertions.assertNotNull(associations, "La liste des associations ne peut être nulle");
        Assertions.assertEquals(5, associations.getSize(), "La taille de la page doit être de 5");
        Assertions.assertEquals(0, associations.getNumber(), "Le numéro de la page doit être 0");
        Assertions.assertEquals(2, associations.getTotalPages(),"Le nombre total de page doit être 2");
        Assertions.assertEquals(10, associations.getTotalElements(),"Le nombre total d'élément doit être 10");

        Page<ReadAssociationDTO> associationsInMfb = assoRepo.searchAssociations("", PageRequest.of(0, 5));
        Assertions.assertNotNull(associationsInMfb, "La liste des associations de MFB ne peut être nulle");
        Assertions.assertEquals(9, associationsInMfb.getTotalElements(),"Le nombre total d'associations dans mfb est 9");

        Page<ReadAssociationDTO> associationsInMPE = assoRepo.searchAssociations("", PageRequest.of(0, 5));
        Assertions.assertNotNull(associationsInMPE, "La liste des associations de MPE ne peut être nulle");
        Assertions.assertEquals(1, associationsInMPE.getTotalElements(),"Le nombre total d'associations dans MPE est 1");

        Page<ReadAssociationDTO> associationsContainingMutuelle = assoRepo.searchAssociations(StringUtils.stripAccentsToUpperCase("mUtûèlLé"), PageRequest.of(0, 5));
        Assertions.assertNotNull(associationsContainingMutuelle, "La liste des associations de contenant le mot clé Mutuelle ne peut être nulle");
        Assertions.assertEquals(4, associationsContainingMutuelle.getTotalElements(),"Le nombre total d'associations contenant le mot clé Mutuelle est 4");

        typeRepo.deleteAll();
        strRepo.deleteAll();
        assoRepo.deleteAll();
    }
}