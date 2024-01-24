package rigeldevsolutions.gestasso.reportmodule.controller;

import rigeldevsolutions.gestasso.archivemodule.controller.service.AbstractDocumentService;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.response.Base64FileDto;
import rigeldevsolutions.gestasso.reportmodule.config.JasperReportConfig;
import rigeldevsolutions.gestasso.reportmodule.service.IServiceReport;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.Base64ToFileConverter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController @RequestMapping(path = "/reports") @RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
public class ReportRestController
{
    private final IServiceReport jrService;
    private final JasperReportConfig jrConfig;
    private final AbstractDocumentService docService;

}
