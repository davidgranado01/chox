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

public class ClaimServiceImpl implements ClaimService {

    public static final String PENDING="Pending";
    public static final String IN_PROGRESS="InProgress";
    public static final String COMPLETE="Complete";
    public static final String CANCELLED="Cancelled";
    public static final String NEW_CLAIM="1st Notification";
    
    public List listAllClaims() {
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(Claim.class);
        List claims =  criteria.list();
        HibernateUtil.closeSession();
        return claims;
    }

    public List listClaimsByStatus(String status) {
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(Claim.class).add(Restrictions.eq("status", status));
        List claims =  criteria.list();
        HibernateUtil.closeSession();  
        return claims;
    }
    
    public Long getCountByStatus(String status)
    {
        Session currentSession = HibernateUtil.currentSession();
        Long count = (Long)currentSession.createQuery("select count(*) from Claim where status = '" + status +"'").uniqueResult();
        HibernateUtil.closeSession();
        return count;
    }
    
    public static Boolean isClaimExist(String CHOClaimId){
        Boolean isExist = false;
        
        /*
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(Claim.class).add(Restrictions.eq("choReference", CHOClaimId));
        
        if(criteria.list().size()>0){
            isExist = true;
        }
        */
        return isExist;
    }
    
    public static Claim getClaimByCHOReferenceNumber(String CHOClaimId){
        Claim claim = new Claim();
        
        /*
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(Claim.class).add(Restrictions.eq("choReference", CHOClaimId));
        
        */
        return claim;
    }
    
    public ArrayList<XMLParseResult> processClaimXMLFile(File claimXMLFile, String sUpdateType, Boolean isAllowPartialUpload) {
        return XmlProcessController.XMLValidationProcess(claimXMLFile, sUpdateType, isAllowPartialUpload);
    }
    
    public static XMLParseResult saveClaimForXMLUploader(Session currentSession, XMLParseResult xmlParseResult){
        
        Claim claim = xmlParseResult.getClaim();
        
        xmlParseResult.getClaim().setCreatedBy(WebUserServiceImpl.getCurrentUser());
        xmlParseResult.getClaim().setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
        xmlParseResult.getClaim().setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
        xmlParseResult.getClaim().setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

        xmlParseResult.getClaim().setInsurer(xmlParseResult.getClaim().getInsurer());
        xmlParseResult.getClaim().setChorganisation(xmlParseResult.getClaim().getChorganisation());
        xmlParseResult.getClaim().setLineOfBusiness(xmlParseResult.getClaim().getLineOfBusiness());
        xmlParseResult.getClaim().setCustomer(xmlParseResult.getClaim().getCustomer());
        xmlParseResult.getClaim().setIncident(xmlParseResult.getClaim().getIncident());
        xmlParseResult.getClaim().setInvoice(xmlParseResult.getClaim().getInvoice());
        xmlParseResult.getClaim().setThirdParty(xmlParseResult.getClaim().getThirdParty());
        //xmlParseResult.getClaim().setVehicleHire(xmlParseResult.getVehiclehires().get(0));
        

        try{
            currentSession.saveOrUpdate(xmlParseResult.getClaim());
        } catch (Exception e) {
            xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
        }
        
        return xmlParseResult;
    }
}












