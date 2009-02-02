/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.report;

import chox.services.DataService;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Emmanuel
 */
public interface Report {
    
    void setExternalParameter(Map parameters);
    void setDataService(DataService dataService);
    HashMap getReportParameters();
    String getReportTemplateFileName();
    
    InputStream build();

}
