package chox.services;

import chox.Util.RoleHelper;
import chox.data.ClaimSearchCriteria;
import chox.model.Claim;
import chox.model.ClaimStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

public class ClaimServiceImpl extends SecureDataService implements ClaimService, Serializable {

    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "InProgress";
    public static final String COMPLETE = "Complete";
    public static final String CANCELLED = "Cancelled";
    public static final String NEW_CLAIM = "1st Notification";

    public ClaimServiceImpl() {
        super();
        return;
    }

    public Claim getClaim(int id) {
        return (Claim) get(Claim.class, id);
    }

    public void updateClaim(Claim claim) {
        claim.setClaimNumber(claim.getClaimNumber().trim());
        save(claim);
    }
    
    public Long getCountByStatus(String status, boolean isCheckWorkGroup, boolean isCheckOwnership) {

        String q = "select count(*) from Claim where status = '" + status + "'";

        if(isCheckWorkGroup){
            
            // WORKGROUP FILTER
            if(RoleHelper.isUserCheckByWorkgroup(getCurrentUser())){
                q += " And workgroup.id in (select workgroup.id from UserWorkgroup Where user.id="+getCurrentUser().getId()+")";
            }

        }
        
        if(isCheckOwnership){
            
            if(RoleHelper.isEditableByOwnership(getCurrentUser())){
                q += " And claimOwner.id ="+getCurrentUser().getId();
            }
            
        }

        return getCount(q);
    }

    public Long getNonDEPaymentLogCount() {
        return (long) 0;
    }

    public Long getPenaltyChargeAppliedCount() {
        String q = "select count(*) from Claim as c inner join c.invoice as iv where " + "c.status <> '" + ClaimStatus.INVOICE_PAYMENT_LOGGED + "' AND " + "c.status <> '" + ClaimStatus.INVOICE_REJECTED_ACCEPTED + "' AND " + "c.status <> '" + ClaimStatus.INVOICE_PAYMENT_RECEIVED + "' AND " + "c.status <> '" + ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT + "' AND " + "c.status <> '" + ClaimStatus.CLAIM_CLOSED + "' " + "AND iv.penaltyAlertQty >= 0" + " AND day(current_date() - iv.createdDate) + 1 > ((iv.penaltyAlertQty + 1) * 30)";
        return getCount(q);
    }

    public Long getHireUpdateWarningCountNumber(boolean isCheckWorkGroup, boolean isCheckOwnership) {
        
        String q = "select count(*) from Claim c where size(c.notifications) > 0 ";
        q += "And (c.status = 'ClaimReferredToEngineer' ";
        q += "Or c.status = 'ClaimReferredToFNOL' ";
        q += "Or c.status = 'ClaimRejectionContested' ";
        q += "Or c.status = 'ClaimPending' ";
        q += "Or c.status = 'AwaitingCarHireInfo' ";
        q += "Or c.status = 'ClaimRejected' ";
        q += "Or c.status = 'ClaimUpdatedByEngineer' ";
        q += "Or c.status = 'ClaimUnacknowledgedRouted') ";

        if(isCheckWorkGroup){

            // WORKGROUP
            if(RoleHelper.isUserCheckByWorkgroup(getCurrentUser())){
                q += " And workgroup.id in (select workgroup.id from UserWorkgroup Where user.id="+getCurrentUser().getId()+")";
            }
        }

        if(isCheckOwnership){
            
            if(RoleHelper.isEditableByOwnership(getCurrentUser())){
                q += " And claimOwner.id ="+getCurrentUser().getId();
            }
            
        }
        
        return getCount(q);
    }

    public Long getECDCountByClaimId(int claimId) {
        String q = "select count(*) from HireMonitoringEcd where claim.id = '" + claimId + "'";
        return getCount(q);
    }

    public Long getClaimCountByClaimNumber(String claimNumber, int claimId) {
        String q = "select count(*) from Claim where claimNumber = '" + claimNumber + "' And id != '" + claimId + "'";
        return getCount(q);
    }

    public List getOtherClaimsByClaimNumber(String claimNumber, int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("claimNumber", claimNumber));
        criteria.add(Restrictions.ne("id", claimId));
        List result = this.findByCriteria(criteria);
        return result;
    }

    public Integer getCountOfClaimByVRN(String strVRN, int claimId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.createCriteria("customer").add(Restrictions.like("vehicleRegistration", strVRN).ignoreCase());
        criteria.add(Expression.ne("id", claimId));
        List result = findByCriteria(criteria);
        Integer totalCount = (Integer) result.get(0);
        return totalCount;


    }

    public Boolean isCustomerClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit) {

        Boolean bFlag = false;

        if (!strClaimNumber.equalsIgnoreCase("")) {
            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
            criteria.setProjection(Projections.rowCount());
            criteria.createCriteria("customer").add(Restrictions.like("claimReference", strClaimNumber).ignoreCase());
            if (isClaimExit) {
                criteria.add(Expression.ne("id", claimId));
            }
            List result = findByCriteria(criteria);

            Integer totalCount = (Integer) result.get(0);
            bFlag = totalCount > 0;
        }

        return bFlag;

    }

    public Boolean isThirdPartyClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit) {

        Boolean bFlag = false;

        if (!strClaimNumber.equalsIgnoreCase("")) {
            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
            criteria.setProjection(Projections.rowCount());
            criteria.createCriteria("thirdParty").add(Restrictions.like("claimReference", strClaimNumber).ignoreCase());
            if (isClaimExit) {
                criteria.add(Expression.ne("id", claimId));
            }
            List result = findByCriteria(criteria);

            Integer totalCount = (Integer) result.get(0);
            bFlag = totalCount > 0;
        }

        return bFlag;
    }

    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria) {
        return searchClaims(searchCriteria, 0, Integer.MAX_VALUE, "", "");
    }

    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria, int start, int limit, String sort, String dir) {

        Criteria criteria = getSession().createCriteria(Claim.class)
                .createAlias("this.invoice", "iv", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.customer", "cs", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.workgroup", "wg", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.thirdParty", "tp", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.vehicleHire", "vh", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.chorganisation", "cho", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.createdBy", "cb", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.hireMonitoringDetail", "hmd", CriteriaSpecification.LEFT_JOIN)
                .createAlias("this.insurer", "ins", CriteriaSpecification.LEFT_JOIN);

        if(searchCriteria.getIsWorkgroupCheck()){
            if(RoleHelper.isUserCheckByWorkgroup(getCurrentUser())){
                criteria.add(Restrictions.sqlRestriction("workgroup_id in (select workgroup_id from user_workgroup where user_id ="+getCurrentUser().getId()+")"));
            }
        }
        
        if(searchCriteria.getIsOwnerShipCheck()){
            if(RoleHelper.isEditableByOwnership(getCurrentUser())){
                criteria.add(Restrictions.eq("claimOwner.id", getCurrentUser().getId()));
            }
            
        }

        if (searchCriteria.getClaimOwnerId() > 0) {
            criteria.add(Restrictions.eq("claimOwner.id", searchCriteria.getClaimOwnerId()));
        }
        
        if (searchCriteria.getSupplierReference() != null && !searchCriteria.getSupplierReference().isEmpty()) {
            String sSupplierRef = "%"+searchCriteria.getSupplierReference()+"%";
            criteria.add(Restrictions.like("choReference", sSupplierRef).ignoreCase());
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
        
        if (searchCriteria.getWorkgroupId() > 0) {
            criteria.add(Restrictions.eq("wg.id", searchCriteria.getWorkgroupId()));
        }

        if (searchCriteria.getIsAnomalies()) {

            Set AnomaliesStatus = new HashSet();
            AnomaliesStatus.add(ClaimStatus.CLAIM_REF_TO_ENG);
            AnomaliesStatus.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
            AnomaliesStatus.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
            AnomaliesStatus.add(ClaimStatus.CLAIM_PENDING);
            AnomaliesStatus.add(ClaimStatus.AWAITING_CAR_HIRE_INFO);
            AnomaliesStatus.add(ClaimStatus.CLAIM_REJECTED);
            AnomaliesStatus.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
            AnomaliesStatus.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            
            criteria.add(Restrictions.sizeGt("notifications", 0));
            criteria.add(Restrictions.in("status", AnomaliesStatus));

        }

        if (searchCriteria.getIspenaltyChargeApplied()) {
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_LOGGED));
            criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_CLOSED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_REJECTED_ACCEPTED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_RECEIVED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
            criteria.add(Restrictions.ge("iv.penaltyAlertQty", 0));
            criteria.add(Restrictions.sqlRestriction("extract(day from current_date- iv1_.created_date) + 1 >(iv1_.panalty_alert_qty+1)*30"));
        }
        
        if (searchCriteria.getInvoiceNumber() != null && !searchCriteria.getInvoiceNumber().isEmpty()) {
            String sSearchInvoiceNumber = "%"+searchCriteria.getInvoiceNumber()+"%";
            criteria.add(Restrictions.like("iv.claimInvoiceNo", sSearchInvoiceNumber).ignoreCase());
        }

        if (searchCriteria.getClaimNumber() != null && !searchCriteria.getClaimNumber().isEmpty()) {
            String sSearchClaimNumber = "%"+searchCriteria.getClaimNumber()+"%";
            criteria.add(Restrictions.like("claimNumber", sSearchClaimNumber).ignoreCase());
        }

        if (searchCriteria.getCustomerVrn()!= null && !searchCriteria.getCustomerVrn().isEmpty()) {
            String sCustomerVrn = "%"+searchCriteria.getCustomerVrn().replaceAll(" ", "")+"%";
            criteria.add(Restrictions.like("cs.vehicleRegistration", sCustomerVrn).ignoreCase());
        }
        
        if (searchCriteria.getThirdPartyVrn() != null && !searchCriteria.getThirdPartyVrn().isEmpty()) {
            String sThirdPartyVrn = "%"+searchCriteria.getThirdPartyVrn().replaceAll(" ", "")+"%";
            criteria.add(Restrictions.like("tp.vehicleRegistration", sThirdPartyVrn).ignoreCase());
        }

        if (searchCriteria.getIsOpenClaim()) {
            criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_REJECTION_ACCEPTED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_REJECTED_ACCEPTED));
            criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_CLOSED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_RECEIVED));
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

        if (searchCriteria.getReviewRequiredDateFrom() != null || searchCriteria.getReviewRequiredDateTo() != null) {
            
            if (searchCriteria.getReviewRequiredDateFrom() != null) {
                
                Date d = searchCriteria.getReviewRequiredDateFrom();
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                criteria.add(Expression.ge("hmd.nextReviewDate", d));
                
            }

            if (searchCriteria.getReviewRequiredDateTo() != null) {

                Date d = searchCriteria.getReviewRequiredDateTo();
                d.setDate(d.getDate());
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                criteria.add(Expression.le("hmd.nextReviewDate", d));

            }

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

        if (searchCriteria.getLastModifiedDateFrom() != null || searchCriteria.getLastModifiedDateTo() != null) {
            
            if (searchCriteria.getLastModifiedDateFrom() != null) {
                Date d = searchCriteria.getLastModifiedDateFrom();
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                criteria.add(Expression.ge("lastModifiedDate", d));
            }
            
            if (searchCriteria.getLastModifiedDateTo() != null) {
                Date d = searchCriteria.getLastModifiedDateTo();
                d.setDate(d.getDate() + 1);
                d.setHours(0);
                d.setMinutes(0);
                d.setSeconds(0);
                criteria.add(Expression.le("lastModifiedDate", d));
            }
            
        }

        criteria.setProjection(Projections.rowCount());

        List totalCountResult = criteria.list();
        Integer totalCount = (Integer) totalCountResult.get(0);

        criteria.setProjection(null);

        if (!sort.isEmpty() && !dir.isEmpty()) {
            if (sort.equalsIgnoreCase("supplierReference")) {
                addSort(criteria, "choReference", dir);
            } else if (sort.equalsIgnoreCase("vehicleRegistration")) {
                addSort(criteria, "tp.vehicleRegistration", dir);
            } else if (sort.equalsIgnoreCase("claimNumber")) {
                addSort(criteria, "claimNumber", dir);
            } else if (sort.equalsIgnoreCase("invoiceAmount")) {
                addSort(criteria, "iv.totalToPay", dir);
            } else if (sort.equalsIgnoreCase("createdDate")) {
                addSort(criteria, "createdDate", dir);
            } else if (sort.equalsIgnoreCase("status")) {
                addSort(criteria, "status", dir);
            } else if (sort.equalsIgnoreCase("statusModifiedDate")) {
                addSort(criteria, "statusModifiedDate", dir);
            } else if (sort.equalsIgnoreCase("workgroup")) {
                addSort(criteria, "wg.name", dir);
            } else if (sort.equalsIgnoreCase("cho")) {
                addSort(criteria, "cho.name", dir);
            } else if (sort.equalsIgnoreCase("insurer")) {
                addSort(criteria, "ins.name", dir);
            } else if (sort.equalsIgnoreCase("createdBy")) {
                addSort(criteria, "cb.firstName", dir);
                addSort(criteria, "cb.lastName", dir);
            } else {
                addSort(criteria, "lastModifiedDate", dir);
            }
        }

        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);

        criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<HashMap> resultMap = criteria.list();

        List claims = new ArrayList<Claim>();

        for (HashMap m : resultMap) {
            claims.add(m.get("this"));
        }

        return new SearchResult(claims, totalCount);
    }

    private void addSort(Criteria criteria, String sort, String dir) {
        if (dir.equalsIgnoreCase("desc")) {
            criteria.addOrder(Order.desc(sort));
        } else {
            criteria.addOrder(Order.asc(sort));
        }
    }

    public Boolean isClaimSupplierReferenceNumberExist(String sClaimReferenceNumber) {

        Boolean bFlag = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.like("choReference", sClaimReferenceNumber.trim()).ignoreCase());
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

    public void saveObjectForXMLUploader(final ClaimResult claimResult) {

        Claim c = claimResult.getClaim();
        c.setClaimNumber(c.getClaimNumber().trim());
        c.setCustomer(claimResult.getClaim().getCustomer());
        c.setThirdParty(claimResult.getClaim().getThirdParty());
        c.setInsurer(claimResult.getClaim().getThirdParty().getInsurer());
        c.setChorganisation(claimResult.getClaim().getChorganisation());
        c.setIncident(claimResult.getClaim().getIncident());
        c.setInvoice(claimResult.getClaim().getInvoice());
        c.setEngineerReport(claimResult.getClaim().getEngineerReport());
        c.setVehicleHire(claimResult.getClaim().getVehicleHire());

        save(c);
    }

    public Boolean isObjectExist(int WorkgroupId) {

        boolean isExist = false;

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);

            criteria.add(Restrictions.eq("workgroup.id", WorkgroupId));

            if (findByCriteria(criteria).size() > 0) {
                isExist = true;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return isExist;

    }

    public boolean isUserHasOpenClaim(int userId){

        boolean isExist = false;

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
            criteria.add(Restrictions.eq("claimOwner.id", userId));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_RECEIVED));
            criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_CLOSED));
            criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_REJECTION_ACCEPTED));
            criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_REJECTED_ACCEPTED));

            if (findByCriteria(criteria).size() > 0) {
                isExist = true;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return isExist;

    }
    
}












