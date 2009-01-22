package chox.services;

import chox.data.ClaimSearchCriteria;
import chox.model.Claim;
import chox.model.ClaimStatus;
import chox.model.XMLParseResult;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class ClaimServiceImpl extends DataService implements ClaimService, Serializable {

    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "InProgress";
    public static final String COMPLETE = "Complete";
    public static final String CANCELLED = "Cancelled";
    public static final String NEW_CLAIM = "1st Notification";
    
    private Map<String,String> sortingMap;

    public Claim getClaim(int id) {
        return (Claim) get(Claim.class, id);
    }

    public void updateClaim(Claim claim) {
        save(claim);
    }
     
    public Long getCountByStatus(String status) {
        String q = "select count(*) from Claim where status = '" + status + "'";
        return getCount(q);
    }
    
    public Long getNonDEPaymentLogCount() {
        return (long)0;
    }
    
    // UPDATED BY CR, 20090122 - 2100
    public Long getPenaltyChargeAppliedCount() {
        
        String q = "select count(*) from Claim as c inner join c.invoice as iv where " 
                + "c.status <> '" + ClaimStatus.INVOICE_PAYMENT_LOGGED + "' AND "
                + "c.status <> '" + ClaimStatus.CLAIM_CLOSED + "' "
                + "AND iv.panaltyAlertQty >= 0"
                + " AND day(current_date() - iv.dateInvoiced) > ((iv.panaltyAlertQty + 1) * 30)";
        
       return getCount(q);
    }
    
    protected Long getCount(String query)
    {
        Long count =  new Long(0);
        List result = query(query);
        
        if(result!= null && !result.isEmpty())
        {
            count = (Long)result.get(0);
        }
        return count;
    }

    public Long getHireUpdateAnomaliesCountNumber() {
        String q = "select count(*) from Claim where is_anomalies = true";
        return getCount(q);
    }
    
    public Long getECDCountByClaimId(int claimId) {
        String q = "select count(*) from HireMonitoringEcd where claim.id = '" + claimId + "'";
        return getCount(q);
    }
    
    public Long getClaimCountByClaimNumber(String claimNumber, int claimId)
    {
        String q = "select count(*) from Claim where claimNumber = '" + claimNumber + "' And id != '"+claimId+"'";
        return getCount(q);
    }

    public Integer getCountOfClaimByVRN(String strVRN, int claimId){               
   
            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
            criteria.setProjection(Projections.rowCount());
            criteria.createCriteria("customer").add(Restrictions.like("vehicleRegistration", strVRN));
            criteria.add( Expression.ne( "id", claimId));
            List result = findByCriteria(criteria);
            Integer totalCount = (Integer)result.get(0);
            return totalCount;

          
    }
    
    public Boolean isCustomerClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit){
        
        Boolean bFlag = false;
        
        if(!strClaimNumber.equalsIgnoreCase("")){
            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
            criteria.setProjection(Projections.rowCount());
            criteria.createCriteria("customer").add(Restrictions.like("claimReference", strClaimNumber));
            if(isClaimExit){
                criteria.add( Expression.ne( "id", claimId));
            }
            List result = findByCriteria(criteria);
            
            Integer totalCount = (Integer)result.get(0);
            bFlag = totalCount > 0;
        }
        
        return bFlag;
        
    }
    
    public Boolean isThirdPartyClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit){
        
        Boolean bFlag = false;
        
        if(!strClaimNumber.equalsIgnoreCase("")){
            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
            criteria.setProjection(Projections.rowCount());
            criteria.createCriteria("thirdParty").add(Restrictions.like("claimReference", strClaimNumber));
            if(isClaimExit){
                criteria.add( Expression.ne( "id", claimId));
            }
            List result = findByCriteria(criteria);
            
            Integer totalCount = (Integer)result.get(0);
            bFlag = totalCount > 0;
        }
        
        return bFlag;        
    }
           
    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria)
    {
        return searchClaims(searchCriteria,0,Integer.MAX_VALUE,"","");
    }

    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria,int start,int limit,String sort,String dir) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class)
            .createAlias("this.invoice", "iv",CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.lineOfBusiness", "lob",CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.thirdParty", "tp",CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.vehicleHire", "vh",CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.chorganisation", "cho",CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.createdBy", "cb",CriteriaSpecification.LEFT_JOIN)
            .createAlias("this.insurer", "ins",CriteriaSpecification.LEFT_JOIN);      
        
        if (searchCriteria.getSupplierReference() != null && !searchCriteria.getSupplierReference().isEmpty()) {
            criteria.add(Restrictions.like("choReference", searchCriteria.getSupplierReference()).ignoreCase());
        }
        if (searchCriteria.getStatus() != null && !searchCriteria.getStatus().isEmpty()) {
            criteria.add(Restrictions.eq("status", searchCriteria.getStatus()));
        }
        if (searchCriteria.getInsurerId() > 0) {
            criteria.add(Restrictions.eq("ins.id", searchCriteria.getInsurerId()));
        }
        if (searchCriteria.getSupplierId() > 0) {            
            criteria.add(Restrictions.eq("cho.id", searchCriteria.getSupplierId()));
        }
        if (searchCriteria.getLineOfBusinessId() > 0) {
            criteria.add(Restrictions.eq("lob.id", searchCriteria.getLineOfBusinessId()));
        }
        if (searchCriteria.getIsAnomalies()) {
            criteria.add(Restrictions.eq("isAnomalies", true));
        }
        if (searchCriteria.getIsPanaltyChargeApplied()) {
           criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_LOGGED));
           criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_CLOSED));
           criteria.add(Restrictions.ge("iv.panaltyAlertQty", 0));
           criteria.add(Restrictions.sqlRestriction("extract(day from current_date- iv1_.date_invoiced)>(iv1_.panalty_alert_qty+1)*30"));
        }
        if (searchCriteria.getInvoiceNumber() != null && !searchCriteria.getInvoiceNumber().isEmpty()) {
            criteria.add(Restrictions.like("iv.claimInvoiceNo", searchCriteria.getInvoiceNumber()).ignoreCase());
        }
        if (searchCriteria.getClaimNumber() != null && !searchCriteria.getClaimNumber().isEmpty()) {
            criteria.add(Restrictions.like("claimNumber", searchCriteria.getClaimNumber()).ignoreCase());
        }
        if (searchCriteria.getVrn() != null && !searchCriteria.getVrn().isEmpty()) {
            String vrn = searchCriteria.getVrn().replaceAll(" ", "");
            criteria.add(Restrictions.like("tp.vehicleRegistration", vrn).ignoreCase());
        }      
 
        if (searchCriteria.getClaimUploadDateFrom() != null) {
            Date d = searchCriteria.getClaimUploadDateFrom();
            d.setHours(0);
            d.setMinutes(0);
            d.setSeconds(0);
            criteria.add(Expression.ge("createdDate", d));
        }
        if (searchCriteria.getClaimUploadDateTo() != null) {
            Date d = searchCriteria.getClaimUploadDateTo();
            d.setDate(d.getDate() + 1);
            d.setHours(0);
            d.setMinutes(0);
            d.setSeconds(0);
            criteria.add(Expression.le("createdDate", d));
        }
        if (searchCriteria.getInvoiceUploadDateFrom() != null || searchCriteria.getInvoiceUploadDateTo() != null) {
            if (searchCriteria.getInvoiceUploadDateFrom() != null) {
                Date d = searchCriteria.getInvoiceUploadDateFrom();
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                criteria.add(Expression.ge("iv.createdDate", d));
            }
            if (searchCriteria.getInvoiceUploadDateTo() != null) {
                Date d = searchCriteria.getInvoiceUploadDateTo();
                d.setDate(d.getDate() + 1);
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                criteria.add(Expression.le("iv.createdDate", d));
            }
        }
        
        if (searchCriteria.getHireDateFrom() != null || searchCriteria.getHireDateTo() != null) {
            if (searchCriteria.getHireDateFrom() != null) {
                Date d = searchCriteria.getHireDateFrom();
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                criteria.add(Expression.ge("vh.rentalStart", d)).add(Expression.le("vh.rentalEnd", d));
            }
            if (searchCriteria.getHireDateTo() != null) {
                Date d = searchCriteria.getHireDateTo();
                d.setDate(d.getDate() + 1);
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                criteria.add(Expression.ge("vh.rentalStart", d)).add(Expression.le("vh.rentalEnd", d));
            }
        }
        //if (searchCriteria.getHireDateFrom() != null && searchCriteria.getHireDateTo() != null) {
        //    criteria.add(Expression.between("createdDate", searchCriteria.getHireDateFrom(), searchCriteria.getHireDateTo()));
        //}
        criteria.setProjection(Projections.rowCount());
       
        List totalCountResult = findByCriteria(criteria);
        Integer totalCount = (Integer)totalCountResult.get(0);
                     
        criteria.setProjection(null);
        if(!sort.isEmpty() && !dir.isEmpty() )
        {
            if(sort.equalsIgnoreCase("supplierReference"))
            {
                addSort(criteria,"choReference",dir);
            }
            else if(sort.equalsIgnoreCase("vehicleRegistration"))
            {
                addSort(criteria,"tp.vehicleRegistration",dir);
            }
            else if(sort.equalsIgnoreCase("claimNumber"))
            {
                 addSort(criteria,"claimNumber",dir);
            }
            else if(sort.equalsIgnoreCase("invoiceAmount"))
            {
                addSort(criteria,"iv.totalToPay",dir);
            }
            else if(sort.equalsIgnoreCase("createdDate"))
            {
                addSort(criteria,"createdDate",dir);
            }
            else if(sort.equalsIgnoreCase("status"))
            {
                addSort(criteria,"status",dir);
            }
            else if(sort.equalsIgnoreCase("lineOfBusiness"))
            {
                addSort(criteria,"lob.name",dir);
            }
            else if(sort.equalsIgnoreCase("cho"))
            {
                 addSort(criteria,"cho.name",dir);
            }
            else if(sort.equalsIgnoreCase("insurer"))
            {
                addSort(criteria,"ins.name",dir);
            }
            else if(sort.equalsIgnoreCase("createdBy"))
            {
                addSort(criteria,"cb.firstName",dir);
                addSort(criteria,"cb.lastName",dir);
            }
            else
            {
                addSort(criteria,"createdDate",dir);
            }
        }

        List result = findByCriteria(criteria,Claim.class,start,limit);
        return new SearchResult(result,totalCount);
    }
    
    private void addSort(DetachedCriteria criteria,String sort,String dir)
    {
        if (dir.equalsIgnoreCase("desc")) {
            criteria.addOrder(Order.desc(sort));
        } else {
            criteria.addOrder(Order.asc(sort));
        }
    }
    
   
    public Boolean isClaimReferenceNumberExist(String sClaimReferenceNumber) {

        Boolean bFlag = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));
        List result = findByCriteria(criteria);

        Integer totalCount = (Integer) result.get(0);
        bFlag = totalCount > 0;


        return bFlag;
    }

    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber) {

        Claim claim = new Claim();
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
            criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));
            claim = (Claim) getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return claim;
    }          
    
    public void saveObjectForXMLUploader( final XMLParseResult xmlParseResult) {

        Claim c = xmlParseResult.getClaim();
        c.setCustomer(xmlParseResult.getClaim().getCustomer());
        c.setThirdParty(xmlParseResult.getClaim().getThirdParty());
        c.setInsurer(xmlParseResult.getClaim().getThirdParty().getInsurer());
        c.setChorganisation(xmlParseResult.getClaim().getChorganisation());
        c.setLineOfBusiness(xmlParseResult.getClaim().getLineOfBusiness());
        c.setIncident(xmlParseResult.getClaim().getIncident());
        c.setInvoice(xmlParseResult.getClaim().getInvoice());
        c.setEngineerReport(xmlParseResult.getClaim().getEngineerReport());
        c.setVehicleHire(xmlParseResult.getClaim().getVehicleHire());

        save(xmlParseResult.getClaim());
    }
   
}












