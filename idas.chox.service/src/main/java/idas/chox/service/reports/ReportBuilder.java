/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author Emmanuel
 */
public interface ReportBuilder {
    
    public ByteArrayOutputStream buildReport(Report report); 

}
