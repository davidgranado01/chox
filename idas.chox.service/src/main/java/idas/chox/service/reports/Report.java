/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.reports;

import idas.chox.data.services.DataService;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public interface Report {

    void setExternalParameter(Map parameters);

    void setDataService(DataService dataService);

    String getReportCode();

    HashMap getReportParameters();

    String getReportTemplateFileName();

    InputStream build();
}
