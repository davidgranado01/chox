package chox.services;

import chox.data.ClaimSearchCriteria;
import chox.model.*;

public interface ClaimService {
    
    public Claim getClaim(int id);
    //Count
    public Long getCountByStatus(String status);
    public Long getNonDEPaymentLogCount();
    public Long getPenaltyChargeAppliedCount();
    public Long getHireUpdateAnomaliesCountNumber();
    public Long getClaimCountByClaimNumber(String claimNumber, int claimId);
    public Long getECDCountByClaimId(int claimId);
    public Integer getCountOfClaimByVRN(String strVRN, int claimId);
    public Boolean isCustomerClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit);
    public Boolean isThirdPartyClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit);
    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria);
    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria,int start,int limit,String sort,String dir);
    public void updateClaim(Claim claim);
    // CARLSON
    public Boolean isClaimSupplierReferenceNumberExist(String sClaimReferenceNumber);
    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber);
   
    public void  saveObjectForXMLUploader(final XMLParseResult xmlParseResult);
}
