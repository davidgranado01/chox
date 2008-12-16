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
    public Long getHireUpdateAnomaliesCount();
    public Long getClaimCountByClaimNumber(String claimNumber);
    public Long getECDCountByClaimId(int claimId);
    //
    public List searchClaims(ClaimSearchCriteria searchCriteria);
    public void updateClaim(Claim claim);
    // CARLSON
    public Boolean isClaimReferenceNumberExist(String sClaimReferenceNumber);
    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber);
    public Claim updateClaimStatus(int claimid, String claimStatus);
}
