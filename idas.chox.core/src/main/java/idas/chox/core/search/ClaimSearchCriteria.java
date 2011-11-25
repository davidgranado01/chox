package idas.chox.core.search;

import idas.chox.core.model.LiabilityStatus;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Emmanuel
 */
public class ClaimSearchCriteria implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimSearchCriteria.class);
    public final static int CLAIM_OWNER_NOT_ASSIGNED = -9;
    public final static String STATUS_ACTIONS_FOR_HANDLERS = "ActionsForHandlers";
    private String supplierReference;
    private int supplierId;
    private String claimNumber;
    private String status;
    private List<String> statusExcludeList;
    private int insurerId;
    private String thirdPartyVrn;
    private String customerVrn;
    private String invoiceNumber;
    private Date claimUploadDateFrom;
    private Date claimUploadDateTo;
    private Date statusModifiedDateFrom;
    private Date statusModifiedDateTo;
    private Date invoiceUploadDateFrom;
    private Date invoiceUploadDateTo;
    private Date hireDateFrom;
    private Date hireDateTo;
    private Date lastModifiedDateFrom;
    private Date lastModifiedDateTo;
    private int workgroupId;
    private boolean isAnomalies;
    private boolean isPenaltyChargeApplied;
    private boolean isInterimPaymentMade;
    private int start;
    private int limit;
    private String sort;
    private String dir;
    private Date reviewRequiredDateFrom;
    private Date reviewRequiredDateTo;
    private boolean isOpenClaim = true;
    private boolean isSupplementaryInvoiceOnly;
    private int claimOwnerId;
    private int supplierClaimOwnerId;
    private boolean isWorkgroupCheck;
    private boolean isOwnerShipCheck;
    private boolean isSupplierOwnerShipCheck;
    private LiabilityStatus liabilityStatus;
    private boolean isLiabilityStatusUpdated;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("supplierReference=").append(supplierReference).append("\n").append("supplierId=").append(supplierId)
                .append("\n").append("claimNumber=").append(claimNumber).append("\n").append("status=").append(status)
                .append("\n").append("insurerId=").append(insurerId).append("\n").append("thirdPartyVrn=").append(thirdPartyVrn)
                .append("\n").append("customerVrn=").append(customerVrn).append("\n").append("invoiceNumber=").append(invoiceNumber)
                .append("\n").append("claimUploadDateFrom=").append(claimUploadDateFrom).append("\n").append("claimUploadDateTo=").append(claimUploadDateTo)
                .append("\n").append("statusModifiedDateFrom=").append(statusModifiedDateFrom).append("\n").append("statusModifiedDateTo=").append(statusModifiedDateTo)
                .append("\n").append("invoiceUploadDateFrom=").append(invoiceUploadDateFrom).append("\n").append("invoiceUploadDateTo=").append(invoiceUploadDateTo)
                .append("\n").append("hireDateFrom=").append(hireDateFrom).append("\n").append("hireDateTo=").append(hireDateTo)
                .append("\n").append("lastModifiedDateFrom=").append(lastModifiedDateFrom).append("\n").append("lastModifiedDateTo=").append(lastModifiedDateTo)
                .append("\n").append("workgroupId=").append(workgroupId).append("\n").append("isAnomalies=").append(isAnomalies)
                .append("\n").append("ispenaltyChargeApplied=").append(isPenaltyChargeApplied).append("\n").append("isInterimPaymentMade=").append(isInterimPaymentMade)
                .append("\n").append("start=").append(start).append("\n").append("limit=").append(limit).append("\n").append("sort=").append(sort)
                .append("\n").append("dir=").append(dir).append("\n").append("reviewRequiredDateFrom=").append(reviewRequiredDateFrom)
                .append("\n").append("reviewRequiredDateTo=").append(reviewRequiredDateTo).append("\n").append("isOpenClaim=").append(isOpenClaim)
                .append("\n").append("claimOwnerId=").append(claimOwnerId).append("\n").append("supplierClaimOwnerId=").append(supplierClaimOwnerId)
                .append("\n").append("isWorkgroupCheck=").append(isWorkgroupCheck).append("\n").append("isOwnerShipCheck=").append(isOwnerShipCheck)
                .append("\n").append("isSupplierOwnerShipCheck=").append(isSupplierOwnerShipCheck).append("\n").append("liabilityStatus=").append(liabilityStatus)
                .append("\n").append("isLiabilityStatusUpdated=").append(isLiabilityStatusUpdated).append("\n").append("isSupplementaryInvoiceOnly=").append(isSupplementaryInvoiceOnly).append("\n");
        return sb.toString();
    }

    public boolean validate() {
        LOG.debug("Validating Claim Search Criteria.");
        if (!isValidString(supplierReference) || !isValidString(claimNumber)
                || !isValidString(status) || !isValidString(thirdPartyVrn) || !isValidString(customerVrn)
                || !isValidString(invoiceNumber)) {
            LOG.debug("Claim Search Criteria is invalid.");
            return false;
        }
        /*       if (!isAlphaNumeric(supplierReference)) supplierReference = "";
        if (!isAlphaNumeric(claimNumber)) claimNumber="";
        if (!isAlphaNumeric(status)) status="";
        if (!isAlphaNumeric(thirdPartyVrn)) thirdPartyVrn = "";
        if (!isAlphaNumeric(customerVrn)) customerVrn="";
        if (!isAlphaNumeric(invoiceNumber)) invoiceNumber=""; */
        LOG.debug("Claim Search Criteria is valid.");
        return true;
    }

    private boolean isValidString(final String s) {
        if (s == null) {
            return true;
        }
        final char[] chars = s.toCharArray();
        for (int x = 0; x < chars.length; x++) {
            final char c = chars[x];
            if (c == 0x0) {
                return false;
            }
        }
        return true;
    }

    private boolean isAlphaNumeric(final String s) {
        if (s == null) {
            return true;
        }
        final char[] chars = s.toCharArray();
        for (int x = 0; x < chars.length; x++) {
            final char c = chars[x];
            if ((c >= 'a') && (c <= 'z')) {
                continue; // lowercase
            }
            if ((c >= 'A') && (c <= 'Z')) {
                continue; // uppercase
            }
            if ((c >= '0') && (c <= '9')) {
                continue; // numeric
            }
            if (c == '%') {
                continue; // allow '%' symbol
            }
            return false;
        }
        return true;
    }

    public boolean isIsSupplementaryInvoiceOnly() {
        return isSupplementaryInvoiceOnly;
    }

    public void setIsSupplementaryInvoiceOnly(boolean isSupplementaryInvoiceOnly) {
        this.isSupplementaryInvoiceOnly = isSupplementaryInvoiceOnly;
    }

    public boolean isLiabilityStatusUpdated() {
        return isLiabilityStatusUpdated;
    }

    public void setLiabilityStatusUpdated(boolean isLiabilityStatusUpdated) {
        this.isLiabilityStatusUpdated = isLiabilityStatusUpdated;
    }

    public LiabilityStatus getLiabilityStatus() {
        return liabilityStatus;
    }

    public void setLiabilityStatus(LiabilityStatus liabilityStatus) {
        this.liabilityStatus = liabilityStatus;
    }

    public Date getReviewRequiredDateFrom() {
        return reviewRequiredDateFrom;
    }

    public void setReviewRequiredDateFrom(Date reviewRequiredDateFrom) {
        this.reviewRequiredDateFrom = reviewRequiredDateFrom;
    }

    public Date getReviewRequiredDateTo() {
        return reviewRequiredDateTo;
    }

    public void setReviewRequiredDateTo(Date reviewRequiredDateTo) {
        this.reviewRequiredDateTo = reviewRequiredDateTo;
    }

    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int gesupplierId) {
        this.supplierId = gesupplierId;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String getCustomerVrn() {
        return customerVrn;
    }

    public void setCustomerVrn(String customerVrn) {
        this.customerVrn = customerVrn;
    }

    public String getThirdPartyVrn() {
        return thirdPartyVrn;
    }

    public void setThirdPartyVrn(String thirdPartyVrn) {
        this.thirdPartyVrn = thirdPartyVrn;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getClaimUploadDateFrom() {
        return claimUploadDateFrom;
    }

    public void setClaimUploadDateFrom(Date claimUploadDateFrom) {
        this.claimUploadDateFrom = claimUploadDateFrom;
    }

    public Date getClaimUploadDateTo() {
        return claimUploadDateTo;
    }

    public void setClaimUploadDateTo(Date claimUploadDateTo) {
        this.claimUploadDateTo = claimUploadDateTo;
    }

    public Date getInvoiceUploadDateFrom() {
        return invoiceUploadDateFrom;
    }

    public void setInvoiceUploadDateFrom(Date invoiceUploadDateFrom) {
        this.invoiceUploadDateFrom = invoiceUploadDateFrom;
    }

    public Date getInvoiceUploadDateTo() {
        return invoiceUploadDateTo;
    }

    public void setInvoiceUploadDateTo(Date invoiceUploadDateTo) {
        this.invoiceUploadDateTo = invoiceUploadDateTo;
    }

    public Date getHireDateFrom() {
        return hireDateFrom;
    }

    public void setHireDateFrom(Date hireDateFrom) {
        this.hireDateFrom = hireDateFrom;
    }

    public Date getHireDateTo() {
        return hireDateTo;
    }

    public void setHireDateTo(Date hireDateTo) {
        this.hireDateTo = hireDateTo;
    }

    public boolean getIsAnomalies() {
        return isAnomalies;
    }

    public void setIsAnomalies(boolean isAnomalies) {
        this.isAnomalies = isAnomalies;
    }

    public boolean getIsPenaltyChargeApplied() {
        return isPenaltyChargeApplied;
    }

    public void setIsPenaltyChargeApplied(boolean isPenaltyChargeApplied) {
        this.isPenaltyChargeApplied = isPenaltyChargeApplied;
    }

    public boolean getIsInterimPaymentMade() {
        return isInterimPaymentMade;
    }

    public void setIsInterimPaymentMade(boolean isInterimPaymentMade) {
        this.isInterimPaymentMade = isInterimPaymentMade;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Date getLastModifiedDateFrom() {
        return lastModifiedDateFrom;
    }

    public void setLastModifiedDateFrom(Date lastModifiedDateFrom) {
        this.lastModifiedDateFrom = lastModifiedDateFrom;
    }

    public Date getLastModifiedDateTo() {
        return lastModifiedDateTo;
    }

    public void setLastModifiedDateTo(Date lastModifiedDateTo) {
        this.lastModifiedDateTo = lastModifiedDateTo;
    }

    public boolean getIsOwnerShipCheck() {
        return isOwnerShipCheck;
    }

    public void setIsOwnerShipCheck(boolean isOwnerShipCheck) {
        this.isOwnerShipCheck = isOwnerShipCheck;
    }

    public boolean getIsSupplierOwnerShipCheck() {
        return isSupplierOwnerShipCheck;
    }

    public void setIsSupplierOwnerShipCheck(boolean isSupplierOwnerShipCheck) {
        this.isSupplierOwnerShipCheck = isSupplierOwnerShipCheck;
    }

    public boolean getIsWorkgroupCheck() {
        return isWorkgroupCheck;
    }

    public void setIsWorkgroupCheck(boolean isWorkgroupCheck) {
        this.isWorkgroupCheck = isWorkgroupCheck;
    }

    public int getClaimOwnerId() {
        return claimOwnerId;
    }

    public void setClaimOwnerId(int claimOwnerId) {
        this.claimOwnerId = claimOwnerId;
    }

    public int getSupplierClaimOwnerId() {
        return supplierClaimOwnerId;
    }

    public void setSupplierClaimOwnerId(int supplierClaimOwnerId) {
        this.supplierClaimOwnerId = supplierClaimOwnerId;
    }

    public boolean getIsOpenClaim() {
        return isOpenClaim;
    }

    public void setIsOpenClaim(boolean isOpenClaim) {
        this.isOpenClaim = isOpenClaim;
    }

    /**
     * @return the statusModifiedDateFrom
     */
    public Date getStatusModifiedDateFrom() {
        return statusModifiedDateFrom;
    }

    /**
     * @param statusModifiedDateFrom the statusModifiedDateFrom to set
     */
    public void setStatusModifiedDateFrom(Date statusModifiedDateFrom) {
        this.statusModifiedDateFrom = statusModifiedDateFrom;
    }

    /**
     * @return the statusModifiedDateTo
     */
    public Date getStatusModifiedDateTo() {
        return statusModifiedDateTo;
    }

    /**
     * @param statusModifiedDateTo the statusModifiedDateTo to set
     */
    public void setStatusModifiedDateTo(Date statusModifiedDateTo) {
        this.statusModifiedDateTo = statusModifiedDateTo;
    }

    public List<String> getStatusExcludeList() {
        return statusExcludeList;
    }

    public void setStatusExcludeList(List<String> statusExcludeList) {
        this.statusExcludeList = statusExcludeList;
    }
}
