package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.data.ClaimSearchCriteria;
import chox.model.Claim;
import chox.model.Incident;
import chox.model.XMLParseResult;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ClaimServiceImpl extends DataService implements ClaimService, Serializable {

    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "InProgress";
    public static final String COMPLETE = "Complete";
    public static final String CANCELLED = "Cancelled";
    public static final String NEW_CLAIM = "1st Notification";

    public Claim getClaim(int id) {
        return (Claim) getCurrentSession().get(Claim.class, id);
    }

    public void updateClaim(Claim claim) {

        getCurrentSession().beginTransaction();
        getCurrentSession().update(claim);
        getCurrentSession().getTransaction().commit();

    }

    public List listAllClaims() {

        Criteria criteria = getCurrentSession().createCriteria(Claim.class);
        List claims = criteria.list();

        return claims;
    }

    public List listClaimsByStatus(String status) {

        Criteria criteria = getCurrentSession().createCriteria(Claim.class).add(Restrictions.eq("status", status));
        List claims = criteria.list();

        return claims;
    }

    public Long getCountByStatus(String status) {
        Long count = (Long) getCurrentSession().createQuery("select count(*) from Claim where status = '" + status + "'").uniqueResult();

        return count;
    }

    public Long getNonDEPaymentLogCount() {
        return (long) 0;
    }

    public Long getHireUpdateAnomaliesCount() {
        return (long) 0;
    }

    public List searchClaims(ClaimSearchCriteria searchCriteria) {
        Criteria criteria = getCurrentSession().createCriteria(Claim.class);

        if (searchCriteria.getSupplierReference() != null && !searchCriteria.getSupplierReference().isEmpty()) {
            criteria.add(Restrictions.like("choReference", searchCriteria.getSupplierReference()).ignoreCase());
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
            criteria.add(Restrictions.like("invoice.id", searchCriteria.getInvoiceNumber()).ignoreCase());
        }
        if (searchCriteria.getClaimNumber() != null && !searchCriteria.getClaimNumber().isEmpty()) {
            criteria.add(Restrictions.like("claimNumber", searchCriteria.getClaimNumber()).ignoreCase());
        }
        if (searchCriteria.getVrn() != null && !searchCriteria.getVrn().isEmpty()) {
            String vrn = searchCriteria.getVrn().replaceAll(" ", "");
            criteria.createCriteria("customer").add(Restrictions.like("vehicleRegistration", vrn).ignoreCase());
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
        criteria.addOrder(Order.asc("createdDate"));

        List claims = criteria.list();
        return claims;
    }

    public Boolean isClaimReferenceNumberExist(
            String sClaimReferenceNumber) {

        Boolean isExist = false;

        try {

            Criteria criteria = getCurrentSession().createCriteria(Claim.class);
            criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));

            if ((criteria.list()).size() > 0) {
                isExist = true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        
        

        return isExist;
    }

    public Claim getClaimByCHOReferenceNumber(
            String sClaimReferenceNumber) {

        Claim claim = new Claim();

        try {

            Criteria criteria = getCurrentSession().createCriteria(
                    Claim.class);
            criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));
            claim = (Claim) criteria.uniqueResult();

        } catch (Throwable e) {
            e.printStackTrace();
        }

        
        
        return claim;
    }
    
    public Long getClaimCoutByClaimNumber(String claimNumber)
    {
        Long count = (Long) getCurrentSession().createQuery("select count(*) from Claim where claimNumber = '" + claimNumber + "'").uniqueResult();

        return count;
    }
   
}












