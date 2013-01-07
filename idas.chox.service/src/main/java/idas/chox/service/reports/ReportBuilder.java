package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author Emmanuel
 */
public interface ReportBuilder {
    
    public ByteArrayOutputStream buildReport(Report report) throws Exception; 

}
