package chox.services;

import chox.data.ClaimSearchCriteria;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import chox.model.*;

public interface ClaimService {
    
    public Claim getClaim(int id);
    public List listAllClaims();
    public List listClaimsByStatus(String status);
    //Count
    public Long getCountByStatus(String status);
    public Long getNonDEPaymentLogCount();
    public Long getPenaltyChargeAppliedCount();
    public Long getHireUpdateAnomaliesCountNumber();
    public Long getClaimCountByClaimNumber(String claimNumber, int claimId);
    public Long getECDCountByClaimId(int claimId);
    public Long getCountOfClaimByVRN(String strVRN, int claimId);
    public Boolean isCustomerClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit);
    public Boolean isThirdPartyClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit);
    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria);
    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria,int start,int limit,String sort,String dir);
    public void updateClaim(Claim claim);
    // CARLSON
    public Boolean isClaimReferenceNumberExist(String sClaimReferenceNumber);
    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber);
    public Claim updateClaimStatus(int claimid, String claimStatus);
}
