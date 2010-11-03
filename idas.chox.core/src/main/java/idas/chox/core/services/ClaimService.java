package idas.chox.core.services;

import idas.chox.core.search.*;
import idas.chox.core.model.*;
import java.util.List;
import java.util.Set;

public interface ClaimService extends DataService {

    public Claim getClaim(int id);

    public Long getClaimCountByClaimNumber(String claimNumber, int claimId);

    public List getOtherClaimsByClaimNumber(String claimNumber, int claimId);

    public Long getECDCountByClaimId(int claimId);

    public Integer getCountOfClaimByVRN(String strVRN, int claimId);

    public Boolean isCustomerClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit);

    public Boolean isThirdPartyClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit);

    public Integer countClaims(ClaimSearchCriteria searchCriteria);

    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria);

    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria, int start, int limit, String sort, String dir);

    public void updateClaim(Claim claim);

    public Boolean revertClaim(int claimId);

    public Boolean isClaimSupplierReferenceNumberExist(String sClaimReferenceNumber);

    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber);

    public Boolean isObjectExist(int WorkgroupId);

    public boolean isUserHasOpenClaim(int UserId);

    public boolean isOpenClaimByWorkgroupsByStatusExist(int insurerId, Set WorkgroupIds, String status);

    public boolean isOpenClaimByWorkgroupExist(int WorkgroupId);

    public boolean isOpenClaimByWorkgroupsByUserExist(int insurerId, Set WorkgroupIds, int userId);

    public boolean isOpenClaimByWorkgroupIdByUserExist(int insurerId, int WorkgroupId, int UserId);

    public void updateSaveLiabilityStatus(Claim claim);

    public String getDaysWithCHOForReview(int id);

    public Boolean switchClaim(int claimId, WebUser webUser);

}
