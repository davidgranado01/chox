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
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Emmanuel
 */
public class ClaimServiceImpl implements ClaimService {

    public List listAllClaims() {
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(Claim.class);
        return criteria.list();
    }

    public List listClaimsByStatus(String status) {
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(Claim.class).add(Restrictions.eq("claimStatus", status));
        return criteria.list();
    }
    
    public static Boolean isClaimExist(String CHOClaimId){
        Boolean isExist = false;
        
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(Claim.class).add(Restrictions.eq("choReference", CHOClaimId));
        
        if(criteria.list().size()>0){
            isExist = true;
        }
        
        return isExist;
    }
    
    public static Claim getClaimByCHOReferenceNumber(String CHOClaimId){
        Claim claim = new Claim();
        
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(Claim.class).add(Restrictions.eq("choReference", CHOClaimId));
        
        
        return claim;
    }
    
    public ArrayList<XMLParseResult> processClaimXMLFile(File claimXMLFile, String sUpdateType, Boolean isAllowPartialUpload) {
        return XmlProcessController.XMLValidationProcess(claimXMLFile, sUpdateType, isAllowPartialUpload);
    }
}
