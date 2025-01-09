package rigeldevsolutions.gestasso.reportmodule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import rigeldevsolutions.gestasso.archivemodule.controller.service.AbstractDocumentService;
import rigeldevsolutions.gestasso.reportmodule.config.JasperReportConfig;
import rigeldevsolutions.gestasso.reportmodule.service.IReportService;

//@Controller @RequestMapping(path = "/reports")
@RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
public class ReportController
{
    private final IReportService jrService;
    private final JasperReportConfig jrConfig;
    private final AbstractDocumentService docService;
}
