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
    private Set<Integer> approvedInvoiceOwnershipSearchParamIds;
    private Set<String> paymentDisputesSearchParamIds;
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
    private boolean anomalies;
    private boolean penaltyChargeApplied;
    private boolean interimPaymentMade;
    private boolean escalatedToSupervisor;
    private int start;
    private int limit;
    private String sort;
    private String dir;
    private Date reviewRequiredDateFrom;
    private Date reviewRequiredDateTo;
    private boolean showOpenClaimsOnly = true;
    private boolean supplementaryInvoiceOnly;
    private boolean penaltyChargesAppliedOnly;
    private Set<Integer> claimOwnerIds;
    private Set<Integer> supplierClaimOwnerIds;
    private boolean manual;
    private boolean workgroupCheck;
    private boolean ownerShipCheck;
    private boolean supplierOwnershipCheck;
    private Set<LiabilityStatus> liabilityStatuses;
    private boolean liabilityStatusUpdated;
    private Set<ClaimType> claimTypes;
    private int finalReviewValue;
    private String filterName;

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
        if (approvedInvoiceOwnershipSearchParamIds != null) {
            sb.append("approvedInvoiceOwnershipSearchParamIds=").append(Arrays.toString(approvedInvoiceOwnershipSearchParamIds.toArray())).append("\n");
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
        sb.append("isAnomalies=").append(anomalies).append("\n")
                .append("ispenaltyChargeApplied=").append(penaltyChargeApplied).append("\n")
                .append("isInterimPaymentMade=").append(interimPaymentMade).append("\n")
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
        sb.append("isWorkgroupCheck=").append(workgroupCheck).append("\n")
                .append("isOwnerShipCheck=").append(ownerShipCheck).append("\n")
                .append("isSupplierOwnerShipCheck=").append(supplierOwnershipCheck).append("\n");
        if (liabilityStatuses != null) {
            sb.append("liabilityStatuses =").append(Arrays.toString(liabilityStatuses.toArray())).append("\n");
        }
        sb.append("isLiabilityStatusUpdated=").append(liabilityStatusUpdated).append("\n")
                .append("isSupplementaryInvoiceOnly=").append(supplementaryInvoiceOnly).append("\n")
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

    public int getFinalReviewValue() {
        return finalReviewValue;
    }

    public void setFinalReviewValue(int finalReviewValue) {
        this.finalReviewValue = finalReviewValue;
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

    public boolean isSupplementaryInvoiceOnly() {
        return supplementaryInvoiceOnly;
    }

    public void setSupplementaryInvoiceOnly(boolean supplementaryInvoiceOnly) {
        this.supplementaryInvoiceOnly = supplementaryInvoiceOnly;
    }

    public boolean isPenaltyChargesAppliedOnly() {
        return penaltyChargesAppliedOnly;
    }

    public void setPenaltyChargesAppliedOnly(boolean penaltyChargesAppliedOnly) {
        this.penaltyChargesAppliedOnly = penaltyChargesAppliedOnly;
    }

    public boolean isLiabilityStatusUpdated() {
        return liabilityStatusUpdated;
    }

    public void setLiabilityStatusUpdated(boolean liabilityStatusUpdated) {
        this.liabilityStatusUpdated = liabilityStatusUpdated;
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

    public boolean isAnomalies() {
        return anomalies;
    }

    public void setAnomalies(boolean anomalies) {
        this.anomalies = anomalies;
    }

    public boolean isPenaltyChargeApplied() {
        return penaltyChargeApplied;
    }

    public void setPenaltyChargeApplied(boolean penaltyChargeApplied) {
        this.penaltyChargeApplied = penaltyChargeApplied;
    }

    public boolean isInterimPaymentMade() {
        return interimPaymentMade;
    }

    public void setInterimPaymentMade(boolean interimPaymentMade) {
        this.interimPaymentMade = interimPaymentMade;
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

    public boolean isOwnerShipCheck() {
        return ownerShipCheck;
    }

    public void setOwnerShipCheck(boolean ownerShipCheck) {
        this.ownerShipCheck = ownerShipCheck;
    }

    public boolean isSupplierOwnerShipCheck() {
        return supplierOwnershipCheck;
    }

    public void setSupplierOwnerShipCheck(boolean supplierOwnerShipCheck) {
        this.supplierOwnershipCheck = supplierOwnerShipCheck;
    }

    public boolean isWorkgroupCheck() {
        return workgroupCheck;
    }

    public void setWorkgroupCheck(boolean workgroupCheck) {
        this.workgroupCheck = workgroupCheck;
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
        return escalatedToSupervisor;
    }

    public void setEscalatedToSupervisor(boolean escalatedToSupervisor) {
        this.escalatedToSupervisor = escalatedToSupervisor;
    }

    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
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

    public Set<Integer> getApprovedInvoiceOwnershipSearchParamIds() {
        return approvedInvoiceOwnershipSearchParamIds;
    }

    public void setApprovedInvoiceOwnershipSearchParamIds(Set<Integer> approvedInvoiceOwnershipSearchParamIds) {
        if (approvedInvoiceOwnershipSearchParamIds.contains(null) || approvedInvoiceOwnershipSearchParamIds.contains(0)) {
            this.approvedInvoiceOwnershipSearchParamIds = null;
        } else {
            this.approvedInvoiceOwnershipSearchParamIds = approvedInvoiceOwnershipSearchParamIds;
        }
    }

    public Set<String> getPaymentDisputesSearchParamIds() {
        return paymentDisputesSearchParamIds;
    }

    public void setPaymentDisputesSearchParamIds(Set<String> paymentDisputesSearchParamIds) {
        if (paymentDisputesSearchParamIds.contains(null) || paymentDisputesSearchParamIds.contains("")) {
            this.paymentDisputesSearchParamIds = null;
        } else {
            this.paymentDisputesSearchParamIds = paymentDisputesSearchParamIds;
        }
    }

    
    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
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

        if (getInsurerIds() != null && !getInsurerIds().isEmpty()) {
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

        if (getSupplierIds() != null && !getSupplierIds().isEmpty()) {
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

        if (getWorkgroupIds() != null && !getWorkgroupIds().isEmpty()) {
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

        if (getSupplierClaimOwnerIds() != null && !getSupplierClaimOwnerIds().isEmpty()) {
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

        if (getClaimOwnerIds() != null && !getClaimOwnerIds().isEmpty()) {
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

        if (getClaimTypes() != null && !getClaimTypes().isEmpty()) {
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

        if (getHireAndRepairSearchParamIds()!= null && !getHireAndRepairSearchParamIds().isEmpty()) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : getHireAndRepairSearchParamIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }
    
    public String getApprovedInvoiceOwnershipSearchParamAsString() {

        if (approvedInvoiceOwnershipSearchParamIds != null && !approvedInvoiceOwnershipSearchParamIds.isEmpty()) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : getApprovedInvoiceOwnershipSearchParamIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }
    
    public String getPaymentDisputesSearchParamAsString() {

        if (paymentDisputesSearchParamIds != null && !paymentDisputesSearchParamIds.isEmpty()) {
            StringBuilder returnString = new StringBuilder();
            for (String i : getPaymentDisputesSearchParamIds()) {
                returnString.append(i).append(",");
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

        if (getLiabilityStatuses() != null && !getLiabilityStatuses().isEmpty()) {
            StringBuilder returnString = new StringBuilder();
            for (LiabilityStatus s : getLiabilityStatuses()) {
                returnString.append(s.getLiablityValue()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }

}
