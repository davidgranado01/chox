package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.data.ClaimSearchCriteria;
import chox.model.Claim;
import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

public class ClaimServiceImpl extends DataService implements ClaimService {

    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "InProgress";
    public static final String COMPLETE = "Complete";
    public static final String CANCELLED = "Cancelled";
    public static final String NEW_CLAIM = "1st Notification";
    
     public Claim getClaim(int id) {
        return (Claim)currentSession.get(Claim.class, id); 
    }

    public List listAllClaims() {

        Criteria criteria = currentSession.createCriteria(Claim.class);
        List claims = criteria.list();

        return claims;
    }

    public List listClaimsByStatus(String status) {

        Criteria criteria = currentSession.createCriteria(Claim.class).add(Restrictions.eq("status", status));
        List claims = criteria.list();

        return claims;
    }

    public Long getCountByStatus(String status) {
        Long count = (Long) currentSession.createQuery("select count(*) from Claim where status = '" + status + "'").uniqueResult();

        return count;
    }

    public List searchClaims(ClaimSearchCriteria searchCriteria) {
        Criteria criteria = currentSession.createCriteria(Claim.class);

        if (searchCriteria.getSupplierReference() != null && !searchCriteria.getSupplierReference().isEmpty()) {
            criteria.add(Restrictions.eq("choReference", searchCriteria.getSupplierReference()));
        }
        if (searchCriteria.getStatus() != null && !searchCriteria.getStatus().isEmpty()) {
            criteria.add(Restrictions.eq("status", searchCriteria.getStatus()));
        }
        if (searchCriteria.getInsurerId() > 0) {
            criteria.add(Restrictions.eq("insurer.id", searchCriteria.getInsurerId()));
        }
        if (searchCriteria.getSupplierId() > 0) {
            criteria.add(Restrictions.eq("chorganisation.id", searchCriteria.getSupplierId()));
        }
        if (searchCriteria.getInvoiceNumber() != null && !searchCriteria.getInvoiceNumber().isEmpty()) {
            criteria.add(Restrictions.eq("invoice.id", searchCriteria.getInvoiceNumber()));
        }
        if (searchCriteria.getClaimNumber() != null && !searchCriteria.getClaimNumber().isEmpty()) {
            try
            {
                Integer claimNumber = Integer.parseInt(searchCriteria.getClaimNumber());
                criteria.add(Restrictions.eq("id", claimNumber));
            }
            catch(NumberFormatException ex)
            {
            
            }
            
        }
        if (searchCriteria.getVrn() != null && !searchCriteria.getVrn().isEmpty()) {
            
            criteria.createCriteria("vehicleHire").add(Restrictions.eq("vehicleRegistration", searchCriteria.getVrn()));
        }
        if (searchCriteria.getClaimUploadDateFrom() != null && searchCriteria.getClaimUploadDateTo() != null) {
            criteria.add(Expression.between("createdDate", searchCriteria.getClaimUploadDateFrom(), searchCriteria.getClaimUploadDateTo()));
        }
        if (searchCriteria.getInvoiceUploadDateFrom() != null && searchCriteria.getInvoiceUploadDateTo() != null) {
            criteria.createCriteria("invoice").add(Expression.between("createdDate", searchCriteria.getInvoiceUploadDateFrom(), searchCriteria.getInvoiceUploadDateTo()));
        }
        //if (searchCriteria.getHireDateFrom() != null && searchCriteria.getHireDateTo() != null) {
        //    criteria.add(Expression.between("createdDate", searchCriteria.getHireDateFrom(), searchCriteria.getHireDateTo()));
        //}

        List claims = criteria.list();
        return claims;
    }

    public Boolean isClaimReferenceNumberExist(
            String sClaimReferenceNumber) {

        Boolean isExist = false;

        try {

            Criteria criteria = currentSession.createCriteria(Claim.class);
            criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));

            if ((criteria.list()).size() > 0) {
                isExist = true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        currentSession.clear();
        currentSession.disconnect();

        return isExist;
    }

    public Claim getClaimByCHOReferenceNumber(
            String sClaimReferenceNumber) {

        Claim claim = new Claim();

        try {

            Criteria criteria = currentSession.createCriteria(
                    Claim.class);
            criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));
            claim = (Claim) criteria.uniqueResult();

        } catch (Throwable e) {
            e.printStackTrace();
        }

        currentSession.clear();
        currentSession.disconnect();
        return claim;
    }

    public ArrayList<XMLParseResult> processClaimXMLFile(File claimXMLFile, Boolean isAllowPartialUpload) {
        XmlProcessController thisCtrl = new XmlProcessController();
        return thisCtrl.XMLValidationProcess(claimXMLFile, isAllowPartialUpload);
    }

    public XMLParseResult saveClaimForXMLUploader(
            XMLParseResult xmlParseResult) {

        xmlParseResult.getClaim().setCreatedBy(getCurrentUser().getId());
        xmlParseResult.getClaim().setCreatedDate(DateHelper.getCurrentTimeStamp());
        xmlParseResult.getClaim().setLastModifiedBy(getCurrentUser().getId());
        xmlParseResult.getClaim().setLastModifiedDate(DateHelper.getCurrentTimeStamp());

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

            try {
                xmlParseResult.getCurrentSession().saveOrUpdate(xmlParseResult.getClaim());
            } catch (Exception e) {
                xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
            }

        }
        return xmlParseResult;
    }

   
}












