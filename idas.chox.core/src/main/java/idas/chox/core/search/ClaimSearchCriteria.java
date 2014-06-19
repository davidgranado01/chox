package idas.chox.core.search;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.ClaimType;
import idas.chox.core.model.LiabilityStatus;

/**
 *
 * @author Emmanuel
 */
public class ClaimSearchCriteria implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimSearchCriteria.class);
    public final static int CLAIM_OWNER_NOT_ASSIGNED = -9;
    public final static String STATUS_ACTIONS_FOR_HANDLERS = "ActionsForHandlers";
    private String supplierReference;
    private Set<Integer> supplierIds;
    private String claimNumber;
    private Set<String> statuses;
    private Set<String> statusExcludeList;
    private Set<Integer> insurerIds;
    private Set<Integer> hireAndRepairSearchParamIds;
    private String thirdPartyVrn;
    private String customerVrn;
    private String invoiceNumber;
    private Date claimUploadDateFrom;
    private Date claimUploadDateTo;
    private Date statusModifiedDateFrom;
    private Date statusModifiedDateTo;
    private Date invoiceUploadDateFrom;
    private Date invoiceUploadDateTo;
    private Date rentalStartDate;
    private Date rentalEndDate;
    private Date lastModifiedDateFrom;
    private Date lastModifiedDateTo;
    private Set<Integer> workgroupIds;
    private boolean isAnomalies;
    private boolean isPenaltyChargeApplied;
    private boolean isInterimPaymentMade;
    private boolean isEscalatedToSupervisor;
    private int start;
    private int limit;
    private String sort;
    private String dir;
    private Date reviewRequiredDateFrom;
    private Date reviewRequiredDateTo;
    private boolean showOpenClaimsOnly = true;
    private boolean isSupplementaryInvoiceOnly;
    private boolean penaltyChargesAppliedOnly;
    private Set<Integer> claimOwnerIds;
    private Set<Integer> supplierClaimOwnerIds;
    private boolean isManual;
    private boolean isWorkgroupCheck;
    private boolean isOwnerShipCheck;
    private boolean isSupplierOwnerShipCheck;
    private Set<LiabilityStatus> liabilityStatuses;
    private boolean isLiabilityStatusUpdated;
    private Set<ClaimType> claimTypes;
    private Boolean finalReviewCho;
    private Boolean finalReviewIns;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("supplierReference=").append(supplierReference).append("\n");
        if (supplierIds != null) {
            sb.append("supplierIds=").append(Arrays.toString(supplierIds.toArray())).append("\n");
        }
        sb.append("claimNumber=").append(claimNumber).append("\n");
        if (statuses != null) {
            sb.append("statuses=").append(Arrays.toString(statuses.toArray())).append("\n");
        }
        if (insurerIds != null) {
            sb.append("insurerIds=").append(Arrays.toString(insurerIds.toArray())).append("\n");
        }
        if (hireAndRepairSearchParamIds != null) {
            sb.append("hireAndRepairSearchParamIds=").append(Arrays.toString(hireAndRepairSearchParamIds.toArray())).append("\n");
        }
        sb.append("thirdPartyVrn=").append(thirdPartyVrn).append("\n")
                .append("customerVrn=").append(customerVrn).append("\n")
                .append("invoiceNumber=").append(invoiceNumber).append("\n")
                .append("claimUploadDateFrom=").append(claimUploadDateFrom).append("\n")
                .append("claimUploadDateTo=").append(claimUploadDateTo).append("\n")
                .append("statusModifiedDateFrom=").append(statusModifiedDateFrom).append("\n")
                .append("statusModifiedDateTo=").append(statusModifiedDateTo).append("\n")
                .append("invoiceUploadDateFrom=").append(invoiceUploadDateFrom).append("\n")
                .append("invoiceUploadDateTo=").append(invoiceUploadDateTo).append("\n")
                .append("hireDateFrom=").append(rentalStartDate).append("\n")
                .append("hireDateTo=").append(rentalEndDate).append("\n")
                .append("lastModifiedDateFrom=").append(lastModifiedDateFrom).append("\n")
                .append("lastModifiedDateTo=").append(lastModifiedDateTo).append("\n");
        if (workgroupIds != null) {
            sb.append("workgroupIds=").append(Arrays.toString(workgroupIds.toArray())).append("\n");
        }
        sb.append("isAnomalies=").append(isAnomalies).append("\n")
                .append("ispenaltyChargeApplied=").append(isPenaltyChargeApplied).append("\n")
                .append("isInterimPaymentMade=").append(isInterimPaymentMade).append("\n")
                .append("start=").append(start).append("\n")
                .append("limit=").append(limit).append("\n")
                .append("sort=").append(sort).append("\n")
                .append("dir=").append(dir).append("\n")
                .append("reviewRequiredDateFrom=").append(reviewRequiredDateFrom).append("\n")
                .append("reviewRequiredDateTo=").append(reviewRequiredDateTo).append("\n")
                .append("isOpenClaim=").append(showOpenClaimsOnly).append("\n");
        if (claimOwnerIds != null) {
            sb.append("claimOwnerIds=").append(Arrays.toString(claimOwnerIds.toArray())).append("\n");
        }
        if (supplierClaimOwnerIds != null) {
            sb.append("supplierClaimOwnerIds=").append(Arrays.toString(supplierClaimOwnerIds.toArray())).append("\n");
        }
        sb.append("isWorkgroupCheck=").append(isWorkgroupCheck).append("\n")
                .append("isOwnerShipCheck=").append(isOwnerShipCheck).append("\n")
                .append("isSupplierOwnerShipCheck=").append(isSupplierOwnerShipCheck).append("\n");
        if (liabilityStatuses != null) {
            sb.append("liabilityStatuses =").append(Arrays.toString(liabilityStatuses.toArray())).append("\n");
        }
        sb.append("isLiabilityStatusUpdated=").append(isLiabilityStatusUpdated).append("\n")
                .append("isSupplementaryInvoiceOnly=").append(isSupplementaryInvoiceOnly).append("\n")
                .append("penaltyChargesAppliedOnly=").append(penaltyChargesAppliedOnly).append("\n");
        if (claimTypes != null) {
            sb.append("claimTypes=").append(Arrays.toString(claimTypes.toArray())).append("\n");
        }
        return sb.toString();
    }

    public boolean validate() {
        LOG.debug("Validating Claim Search Criteria.");
        if (!isValidString(supplierReference) || !isValidString(claimNumber)
                || !isValidString(thirdPartyVrn) || !isValidString(customerVrn)
                || !isValidString(invoiceNumber)) {
            LOG.debug("Claim Search Criteria is invalid.");
            return false;
        }
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

    public Set<ClaimType> getClaimTypes() {
        return claimTypes;
    }

    public void setClaimTypes(Set<ClaimType> claimTypes) {
        if (claimTypes.contains(null)) {
            this.claimTypes = null;
        } else {
            this.claimTypes = claimTypes;
        }
    }

    public boolean isIsSupplementaryInvoiceOnly() {
        return isSupplementaryInvoiceOnly;
    }

    public void setIsSupplementaryInvoiceOnly(boolean isSupplementaryInvoiceOnly) {
        this.isSupplementaryInvoiceOnly = isSupplementaryInvoiceOnly;
    }

    public boolean isPenaltyChargesAppliedOnly() {
        return penaltyChargesAppliedOnly;
    }

    public void setPenaltyChargesAppliedOnly(boolean penaltyChargesAppliedOnly) {
        this.penaltyChargesAppliedOnly = penaltyChargesAppliedOnly;
    }

    public boolean isLiabilityStatusUpdated() {
        return isLiabilityStatusUpdated;
    }

    public void setLiabilityStatusUpdated(boolean isLiabilityStatusUpdated) {
        this.isLiabilityStatusUpdated = isLiabilityStatusUpdated;
    }

    public Set<LiabilityStatus> getLiabilityStatuses() {
        return liabilityStatuses;
    }

    public void setLiabilityStatuses(Set<LiabilityStatus> liabilityStatuses) {
        if (liabilityStatuses.contains(null)) {
            this.liabilityStatuses = null;
        } else {
            this.liabilityStatuses = liabilityStatuses;
        }
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

    public Set<Integer> getWorkgroupIds() {
        return workgroupIds;
    }

    public void setWorkgroupIds(Set<Integer> workgroupIds) {
        if (workgroupIds.contains(null)) {
            this.workgroupIds = null;
        } else {
            this.workgroupIds = workgroupIds;
        }
    }

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    public Set<Integer> getSupplierIds() {
        return supplierIds;
    }

    public void setSupplierIds(Set<Integer> supplierIds) {
        if (supplierIds.contains(null) || supplierIds.contains(0)) {
            this.supplierIds = null;
        } else {
            this.supplierIds = supplierIds;
        }
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public Set<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<String> statuses) {
        if (statuses.contains(null) || statuses.contains("")) {
            this.statuses = null;
        } else {
            this.statuses = statuses;
        }
    }

    public Set<Integer> getInsurerIds() {
        return insurerIds;
    }

    public void setInsurerIds(Set<Integer> insurerIds) {
        if (insurerIds.contains(null) || insurerIds.contains(0)) {
            this.insurerIds = null;
        } else {
            this.insurerIds = insurerIds;
        }
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

    public Date getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(Date rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public Date getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(Date rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
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

    public Set<Integer> getClaimOwnerIds() {
        return claimOwnerIds;
    }

    public void setClaimOwnerIds(Set<Integer> claimOwnerIds) {
        if (claimOwnerIds.contains(null)) {
            this.claimOwnerIds = null;
        } else {
            this.claimOwnerIds = claimOwnerIds;
        }
    }

    public Set<Integer> getSupplierClaimOwnerIds() {
        return supplierClaimOwnerIds;
    }

    public void setSupplierClaimOwnerIds(Set<Integer> supplierClaimOwnerIds) {
        if (supplierClaimOwnerIds.contains(null)) {
            this.supplierClaimOwnerIds = null;
        } else {
            this.supplierClaimOwnerIds = supplierClaimOwnerIds;
        }
    }

    public boolean isShowOpenClaimsOnly() {
        return showOpenClaimsOnly;
    }

    public void setShowOpenClaimsOnly(boolean showOpenClaimsOnly) {
        this.showOpenClaimsOnly = showOpenClaimsOnly;
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

    public Set<String> getStatusExcludeList() {
        return statusExcludeList;
    }

    public void setStatusExcludeList(Set<String> statusExcludeList) {
        this.statusExcludeList = statusExcludeList;
    }

    public boolean isEscalatedToSupervisor() {
        return isEscalatedToSupervisor;
    }

    public void setEscalatedToSupervisor(boolean isEscalatedToSupervisor) {
        this.isEscalatedToSupervisor = isEscalatedToSupervisor;
    }

    public boolean isIsManual() {
        return isManual;
    }

    public void setIsManual(boolean isManual) {
        this.isManual = isManual;
    }

    public Boolean isFinalReviewCho() {
        return finalReviewCho;
    }

    public void setFinalReviewCho(Boolean finalReviewCho) {
        this.finalReviewCho = finalReviewCho;
    }

    public Boolean isFinalReviewIns() {
        return finalReviewIns;
    }

    public void setFinalReviewIns(Boolean finalReviewIns) {
        this.finalReviewIns = finalReviewIns;
    }

    public Set<Integer> getHireAndRepairSearchParamIds() {
        return hireAndRepairSearchParamIds;
    }

    public void setHireAndRepairSearchParamIds(Set<Integer> hireAndRepairSearchParamIds) {
        if (hireAndRepairSearchParamIds.contains(null) || hireAndRepairSearchParamIds.contains(0)) {
            this.hireAndRepairSearchParamIds = null;
        } else {
            this.hireAndRepairSearchParamIds = hireAndRepairSearchParamIds;
        }
    }
    
       /*
     * Please note this method will return only Claim statuses from the
     * loaded(model) claimSearchCriteria and not from available Claim statuses.
     */
    public String getClaimStatusesAsString() {

        if (getStatuses() != null) {
            StringBuilder returnString = new StringBuilder();
            for (String c : getStatuses()) {
                returnString.append(c).append(",");
            }
            return returnString.toString().isEmpty() ? "" : returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }

    /*
     * Please note this method will return only Insurer Ids from the
     * loaded(model) claimSearchCriteria and not from available Insurer Id.
     */
    public String getInsurerIdsAsString() {

        if (getInsurerIds() != null) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : getInsurerIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }

    /*
     * Please note this method will return only Supplier Ids from the
     * loaded(model) claimSearchCriteria and not from available Supplier Id.
     */
    public String getSupplierIdsAsString() {

        if (getSupplierIds() != null) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : getSupplierIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }

    /*
     * Please note this method will return only Workgroup Ids from the
     * loaded(model) claimSearchCriteria and not from available Workgroup Id.
     */
    public String getWorkgroupIdsAsString() {

        if (getWorkgroupIds() != null) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : getWorkgroupIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }
    
    /*
     * Please note this method will return only Supplier Claim owner Ids from the
     * loaded(model) claimSearchCriteria and not from available Supplier Claim owner Id.
     */
    public String getSupplierClaimOwnerIdsAsString() {

        if (getSupplierClaimOwnerIds() != null) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : getSupplierClaimOwnerIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }
    
     /*
     * Please note this method will return only Supplier Claim owner Ids from the
     * loaded(model) claimSearchCriteria and not from available Supplier Claim owner Id.
     */
    public String getClaimOwnerIdsAsString() {

        if (getClaimOwnerIds() != null) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : getClaimOwnerIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }
    
    /*
     * Please note this method will return only claim types from the
     * loaded(model) claimSearchCriteria and not from available LiabilityStatus.
     */
    public String getClaimTypesValueAsString() {

        if (getClaimTypes() != null) {
            StringBuilder returnString = new StringBuilder();
            for (ClaimType c : getClaimTypes()) {
                returnString.append(c.getClaimTypeValue()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }
    
    /*
     * Please note this method will return only hire and repair search param from the
     * loaded(model) claimSearchCriteria and not from available hire and repair search param.
     */
    public String getHireAndRepairSearchParamAsString() {

        if (getHireAndRepairSearchParamIds()!= null) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : getHireAndRepairSearchParamIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }
    
    /*
     * Please note this method will return only Liability value from the
     * loaded(model) claimSearchCriteria and not from available LiabilityStatus.
     */
    public String getLiabilityStatusesValueAsString() {

        if (getLiabilityStatuses() != null) {
            StringBuilder returnString = new StringBuilder();
            for (LiabilityStatus s : getLiabilityStatuses()) {
                returnString.append(s.getLiablityValue()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }

    
}
