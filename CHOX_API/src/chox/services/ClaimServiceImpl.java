/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.data.HibernateUtil;
import chox.model.Claim;
import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 *
 * @author Emmanuel
 */
public class ClaimServiceImpl implements ClaimService {
    
    public List listAllClaims()
    {
       Session currentSession = HibernateUtil.currentSession();
       Criteria criteria = currentSession.createCriteria(Claim.class);       
       return criteria.list();
    }   
    
     public ArrayList<XMLParseResult> processClaimXMLFile(File claimXMLFile, String sUpdateType, Boolean isAllowPartialUpload) {
        return XmlProcessController.XMLValidationProcess(claimXMLFile, sUpdateType, isAllowPartialUpload);
    }
}
