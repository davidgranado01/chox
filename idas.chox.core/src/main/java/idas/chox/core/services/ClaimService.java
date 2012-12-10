package idas.chox.core.services;

import java.util.List;
import java.util.Set;

import idas.chox.core.model.*;
import idas.chox.core.search.*;

public interface ClaimService extends DataService {

    public Claim getClaim(int id);

    public Long getClaimCountByClaimNumber(String claimNumber, int claimId);

    public List getOtherClaimsByClaimNumber(String claimNumber, int claimId);

    public Long getECDCountByClaimId(int claimId);

    public Integer getCountOfClaimByVRN(String strVRN, int claimId);

    public Integer getCountOfClaimByVRNforNewClaim(String strVRN, Claim claim);

    public Boolean isCustomerClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit);

    public Boolean isThirdPartyClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit);

    public Integer countClaims(ClaimSearchCriteria searchCriteria);

    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria);

    public SearchResult searchClaims(ClaimSearchCriteria searchCriteria, int start, int limit, String sort, String dir);

    public void updateClaim(Claim claim);

    public Boolean revertClaim(int claimId);

    public List getClaimsByCustomerClaimRef(String customerClaimRef, int choId);

    public Boolean isClaimSupplierReferenceNumberExist(String sClaimReferenceNumber);

    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber);

    public Boolean isObjectExist(int WorkgroupId);

    public boolean isUserHasOpenClaim(int UserId);

    public boolean isOpenClaimByWorkgroupsByStatusExist(int insurerId, Set WorkgroupIds, String status);

    public boolean isSubscriberClaimRejectedAndAgreed(int claimId);
        
    public boolean isOpenClaimByWorkgroupExist(int WorkgroupId);

    public boolean isOpenClaimByWorkgroupsByUserExist(int insurerId, Set WorkgroupIds, int userId);

    public boolean isOpenClaimByWorkgroupIdByUserExist(int insurerId, int WorkgroupId, int UserId);

    public void updateSaveLiabilityStatus(Claim claim);

    public String getDaysWithCHOForReview(int id);

    public String getDaysWithInsurerForReview(int id);

    public int getSubscriberClaimDays(int id);

    public int getSubscriberClaimRejects(int id);
    
    public int getFixedFeeClaimDays(int id);

    public int getClaimRejects(int id);
    
    public String getDaysAwaitingLiabilityResolution(int id);

    public void saveClaimWithoutUpdatingLiabilityPayment(Claim claim);

    public List getDuplicateSupplementaryInvoiceClaims(String customerClaimRef, int claimId);
    
    public Claim getOriginalSupplementaryInvoicedClaim(String customerClaimRef);
    
    public int getSubscriberClaimRejectedDays(int claimId);
    
    public int getFixedFeeClaimRejectedDays(int claimId);
    
    public int updateReservationToTicket(String oldReference, String newReference, Integer choId, String sender);
    
    public int updateQueuedTicket(QueuedTicket queuedTicket, Integer choId);
    
    public List<QueuedTicket> getQueuedTicket();
 
    public Claim getClaimByChoIdAndCHOReferenceNumber(Integer choId, String sClaimReferenceNumber);
    
    public Claim updateClaimWithInvalidSessionVersion(Claim claim);
    
    public void updateLiabilityPayment(Claim claim);

    public int getDaysSinceInvoiceUploadToEscalate(Integer claimId);

    public int getNumberOfTimesContestedWithCHOtoEscalate(Integer claimId);
    
    public int getNoOfRejectedClaims(Integer reasonOfRejectionId);

    public String getOverlappingHire(Claim claim);
    
    public int getActivityMonitorRequestInterval();
    
    public boolean isEnableActivityMonitor();
}
