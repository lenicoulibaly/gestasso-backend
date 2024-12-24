package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;

@Service @RequiredArgsConstructor
public class CalculEcheanceService
{
    private final CotisationRepo cotisationRepo;


}
