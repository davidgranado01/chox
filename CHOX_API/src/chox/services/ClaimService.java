package chox.services;

import chox.data.ClaimSearchCriteria;
import chox.model.*;
import java.util.List;

public interface ClaimService {
    
    public Claim getClaim(int id);
    public Long getCountByStatus(String status, boolean isCheckWorkGroup, boolean isCheckOwnership);
    public Long getNonDEPaymentLogCount();
    public Long getPenaltyChargeAppliedCount();
    public Long getHireUpdateWarningCountNumber(boolean isCheckWorkGroup, boolean isCheckOwnership);
    public Long getClaimCountByClaimNumber(String claimNumber, int claimId);
    public List getOtherClaimsByClaimNumber(String claimNumber, int claimId);
    public Long getECDCountByClaimId(int claimId);
    public Integer getCountOfClaimByVRN(String strVRN, int claimId);
    public Boolean isCustomerClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit);
    public Boolean isThirdPartyClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit);
    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria);
    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria,int start,int limit,String sort,String dir);
    public void updateClaim(Claim claim);
    public Boolean isClaimSupplierReferenceNumberExist(String sClaimReferenceNumber);
    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber);
    public void  saveObjectForXMLUploader(final ClaimResult claimResult);
    public Boolean isObjectExist(int WorkgroupId);
    public boolean isUserHasOpenClaim(int userId);
}
