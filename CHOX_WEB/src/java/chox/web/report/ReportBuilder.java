/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.report;

import java.io.InputStream;
import java.util.Map;

/**
 *
 * @author Emmanuel
 */
public interface ReportBuilder {
    
    public InputStream buildReport(Report report); 

}
