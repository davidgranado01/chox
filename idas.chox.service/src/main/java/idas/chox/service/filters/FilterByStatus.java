package idas.chox.service.filters;

import java.util.Arrays;
import java.util.HashSet;

import idas.chox.core.search.ClaimSearchCriteria;

public class FilterByStatus extends BaseFilter {

    private String status;
    private String name;
    private String key;
//    private static final String FINAL_REVIEW_INS = "FinalReviewIns";
//    private static final String FINAL_REVIEW_CHO = "FinalReviewCho";
//    private static final String AWAITING_LIABILITY_RESOLUTION = "AwaitingLiabilityResolution";

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(Boolean isCHO, ClaimSearchCriteria claimSearchCriteria) {
//        ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
        // None of the queue shows closed claims, so the below line is not required.
//        claimSearchCriteria.setShowOpenClaimsOnly(false);
        claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList(getStatus())));
        claimSearchCriteria.setIsManual(getIsManualFilter());
        claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        
        //in case of "Rejected Claims" queue we don't want to display the fixed fee claim types
//        if(getStatus().equals(ClaimStatus.CLAIM_REJECTED) && claimTypeId == -1){
//            Set<ClaimType> claimTypes = EnumSet.of(ClaimType.GTA, 
//                    ClaimType.SUBSCRIBER, 
//                    ClaimType.TPI, 
//                    ClaimType.INSURER_VS_INSURER, 
//                    ClaimType.COLLABORATION_PROTOCOL);
//            claimSearchCriteria.setClaimTypes(claimTypes);
//        }

//        if (insurerId > -1) {
//            claimSearchCriteria.setInsurerIds(new HashSet<Integer>(Arrays.asList(insurerId)));
//        }
//        if (choId > -1) {
//            claimSearchCriteria.setSupplierIds(new HashSet<Integer>(Arrays.asList(choId)));
//        }
//
//        if (claimTypeId > -1) {
//            // Set filter on Claim Type
//            if(ClaimType.FIXED_FEE.getClaimTypeValue() == claimTypeId && getStatus().equals(ClaimStatus.CLAIM_REJECTED)) {
//                // FixedFee rejected claims have their own queue/filter so we don't want to return anything in this filter.
//                // Therefore add a criteria that will return nothing
//                claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList("nosuchstatus")));
//            } else {
//                claimSearchCriteria.setClaimTypes(new HashSet<ClaimType>(Arrays.asList(ClaimType.values()[claimTypeId])));
//            }
//        }

//        if (getKey().equals(AWAITING_LIABILITY_RESOLUTION)) {
//            if (isCHO == null) { // CHOX Admin
//                claimSearchCriteria.setFinalReviewCho(Boolean.FALSE);
//                claimSearchCriteria.setFinalReviewIns(Boolean.FALSE);
//            } else if (isCHO) {
//                claimSearchCriteria.setFinalReviewCho(Boolean.FALSE);
//            } else {
//                claimSearchCriteria.setFinalReviewIns(Boolean.FALSE);
//            }
//        }
        
//        if (getKey().equals(FINAL_REVIEW_INS) || getKey().equals(FINAL_REVIEW_CHO)) {
//            if (isCHO == null) { // CHOX Admin
//                claimSearchCriteria.setFinalReviewCho(Boolean.TRUE);
//                claimSearchCriteria.setFinalReviewIns(Boolean.TRUE);
//            } else if (isCHO) {
//                claimSearchCriteria.setFinalReviewCho(Boolean.TRUE);
//            } else {
//                claimSearchCriteria.setFinalReviewIns(Boolean.TRUE);
//            }
//        }
        
//        if (isCHO != null && !isCHO
//                && (ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO.equals(getStatus())
//                    || ClaimStatus.CLAIM_AWAITING_INVOICE_DATA.equals(getStatus()))) {
//            
//            if (claimTypeId == -1 || ClaimType.INSURER_UPLOAD.getClaimTypeValue() == claimTypeId) {
//                // Insurer should only see Insurer Uploaded Claims in these queue
//                Set<ClaimType> claimTypes = new HashSet<ClaimType>();
//                claimTypes.add(ClaimType.INSURER_UPLOAD);
//                claimSearchCriteria.setClaimTypes(claimTypes);
//            } else {
//                 //insurer should see empty queue
//                claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList("nosuchstatus")));
//            }
//        }

        return claimSearchCriteria;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
   
}
