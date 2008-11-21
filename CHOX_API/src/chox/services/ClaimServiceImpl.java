/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;


import chox.model.Claim;
import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import chox.data.HibernateUtil;
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
    
    public Boolean isClaimReferenceNumberExist(String sClaimReferenceNumber){
        
        Boolean isExist = false;
        
        Session currentSession = HibernateUtil.currentSession();      
        
        try {
            
            Criteria criteria = currentSession.createCriteria(Claim.class);
            criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));
            
            if((criteria.list()).size()>0){
                isExist = true;
            }
    
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        currentSession.clear();
        currentSession.disconnect();

        return isExist;
    }
    
    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber){
        
        Session currentSession = HibernateUtil.currentSession();      
        Claim claim = new Claim();
        
        try {
            
            Criteria criteria = currentSession.createCriteria(Claim.class);
            criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));
            claim = (Claim) criteria.uniqueResult();
            
            /*
            if(claim!=null){
                
                // GET CUSTOMER INFORMATION
                CustomerService custService = new CustomerServiceImpl();
                claim.setCustomer(custService.getCustomerById(1));
                
                
                // GET CHOBAND INFORMATION
                
            }
            */
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        currentSession.clear();
        currentSession.disconnect();
        return claim;
    }
    
    public ArrayList<XMLParseResult> processClaimXMLFile(File claimXMLFile, String sUpdateType, Boolean isAllowPartialUpload) {
        XmlProcessController thisCtrl = new XmlProcessController();
        return thisCtrl.XMLValidationProcess(claimXMLFile, sUpdateType, isAllowPartialUpload);
    }
    
    public XMLParseResult saveClaimForXMLUploader(XMLParseResult xmlParseResult){
        
        xmlParseResult.getClaim().setCreatedBy(WebUserServiceImpl.getCurrentUser());
        xmlParseResult.getClaim().setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
        xmlParseResult.getClaim().setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
        xmlParseResult.getClaim().setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());
        
        if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {
            xmlParseResult.getClaim().setCustomer(xmlParseResult.getClaim().getCustomer());
            xmlParseResult.getClaim().setThirdParty(xmlParseResult.getClaim().getThirdParty());
            xmlParseResult.getClaim().setInsurer(xmlParseResult.getClaim().getInsurer());
            xmlParseResult.getClaim().setChorganisation(xmlParseResult.getClaim().getChorganisation());
            xmlParseResult.getClaim().setLineOfBusiness(xmlParseResult.getClaim().getLineOfBusiness());
            xmlParseResult.getClaim().setIncident(xmlParseResult.getClaim().getIncident());
            xmlParseResult.getClaim().setInvoice(xmlParseResult.getClaim().getInvoice());
            xmlParseResult.getClaim().setEngineerReport(xmlParseResult.getClaim().getEngineerReport());
            xmlParseResult.getClaim().setVehicleHire(xmlParseResult.getClaim().getVehicleHire());

            try{
                xmlParseResult.getCurrentSession().saveOrUpdate(xmlParseResult.getClaim());
            } catch (Exception e) {
                xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
            }
        }
        return xmlParseResult;
    }
}












