package rigeldevsolutions.gestasso.reportmodule.controller;

import rigeldevsolutions.gestasso.archivemodule.controller.service.AbstractDocumentService;
import rigeldevsolutions.gestasso.reportmodule.config.JasperReportConfig;
import rigeldevsolutions.gestasso.reportmodule.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping(path = "/reports") @RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
public class ReportRestController
{
    private final IReportService jrService;
    private final JasperReportConfig jrConfig;
    private final AbstractDocumentService docService;

}
