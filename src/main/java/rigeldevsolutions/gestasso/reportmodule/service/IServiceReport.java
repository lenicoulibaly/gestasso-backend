package rigeldevsolutions.gestasso.reportmodule.service;

import java.util.List;
import java.util.Map;

public interface IServiceReport
{
    byte[] generateReport(String reportName, Map<String, Object> params, List<Object> data, String qrText) throws Exception;
}
