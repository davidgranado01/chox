package idas.chox.service.filters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.search.ClaimSearchCriteria;

public class FilterByStatus extends BaseFilter {

    private String status;
    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(Boolean isCHO, int insurerId, int choId, int claimTypeId) {
        ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
        claimSearchCriteria.setShowOpenClaimsOnly(false);
        claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList(getStatus())));
        claimSearchCriteria.setIsManual(getIsManualFilter());
        claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        
        //in case of "Rejected Claims" queue we don't want to display the fixed fee claim types
        if(getStatus().equals(ClaimStatus.CLAIM_REJECTED) && claimTypeId == -1){
            Set<ClaimType> claimTypes = new HashSet<ClaimType>();
            claimTypes.add(ClaimType.GTA);
            claimTypes.add(ClaimType.SUBSCRIBER);
            claimTypes.add(ClaimType.TPI);
            claimTypes.add(ClaimType.INSURER_VS_INSURER);
            claimSearchCriteria.setClaimTypes(claimTypes);
        }
        
        if (insurerId > -1) {
            claimSearchCriteria.setInsurerIds(new HashSet<Integer>(Arrays.asList(insurerId)));
        }
        if (choId > -1) {
            claimSearchCriteria.setSupplierIds(new HashSet<Integer>(Arrays.asList(choId)));
        }

        if (claimTypeId > -1) {
            // Set filter on Claim Type
            if(ClaimType.FIXED_FEE.getClaimTypeValue() == claimTypeId && getStatus().equals(ClaimStatus.CLAIM_REJECTED)) {
                // FixedFee rejected claims have their own queue/filter so we don't want to return anything in this filter.
                // Therefore give an empty set for the claim types
                claimSearchCriteria.setClaimTypes(new HashSet<ClaimType>());
            } else {
                claimSearchCriteria.setClaimTypes(new HashSet<ClaimType>(Arrays.asList(ClaimType.values()[claimTypeId])));
            }
        }

        if (ClaimStatus.AWAITING_LIABILITY_RESOLUTION.equals(getStatus())) {
            if (isCHO == null) { // CHOX Admin
                claimSearchCriteria.setFinalReviewCho(Boolean.FALSE);
                claimSearchCriteria.setFinalReviewIns(Boolean.FALSE);
            } else if (isCHO) {
                claimSearchCriteria.setFinalReviewCho(Boolean.FALSE);
            } else {
                claimSearchCriteria.setFinalReviewIns(Boolean.FALSE);
            }
        }
        
        if (isCHO != null && !isCHO
                && (ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO.equals(getStatus())
                    || ClaimStatus.CLAIM_AWAITING_INVOICE_DATA.equals(getStatus())) ) {
            // Insurer should only see Insurer Uploaded Claims in these queue
            Set<ClaimType> claimTypes = new HashSet<ClaimType>();
            claimTypes.add(ClaimType.INSURER_UPLOAD);
            claimSearchCriteria.setClaimTypes(claimTypes);
        }

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
