package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import idas.chox.core.model.Claim;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.QueuedTicket;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;

public interface ClaimService extends DataService {

    Claim getClaim(int id);

    int getClaimCountByClaimNumber(String claimNumber, int claimId);

    List getOtherClaimsByClaimNumber(String claimNumber, int claimId);

    int getECDCountByClaimId(int claimId);

    Integer getCountOfClaimByVRN(String strVRN, int claimId);

    Integer getCountOfClaimByVRNforNewClaim(String strVRN, Claim claim);

    Boolean isCustomerClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit);

    Boolean isThirdPartyClaimNumberExist(String strClaimNumber, int claimId, Boolean isClaimExit);

    Integer countClaims(ClaimSearchCriteria searchCriteria);

    SearchResult searchClaims(ClaimSearchCriteria searchCriteria);

    void updateClaim(Claim claim);
    
    void checkRepairBookedInDateAnomaly(Claim claim);

    void checkTotalLossAnomaly(Claim claim);

    Boolean revertClaim(int claimId);

    List getCHOClaimsByCustomerClaimRef(String customerClaimRef, int choId);
    
    List getInsurerClaimsByCustomerClaimRef(String customerClaimRef, int insId);

    Boolean isClaimSupplierReferenceNumberExist(String sClaimReferenceNumber);
    
    Boolean isClaimSupplierReferenceNumberExistForCho(String sClaimReferenceNumber, int choId);
    Boolean isClaimSupplierReferenceNumberExistForChoExternal(String sClaimReferenceNumber, int choId);

    Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber);

    Boolean isObjectExist(int workgroupId);

    boolean isUserHasOpenClaim(int userId);

    boolean isOpenClaimByWorkgroupsByStatusExist(int insurerId, Set workgroupIds, String status);

    boolean isSubscriberClaimRejectedAndAgreed(int claimId);
        
    boolean isOpenClaimByWorkgroupExist(int workgroupId);

    boolean isOpenClaimByWorkgroupsByUserExist(int insurerId, Set workgroupIds, int userId);

    boolean isOpenClaimByWorkgroupIdByUserExist(int insurerId, int workgroupId, int UserId);

    String getDaysWithCHOForReview(int id);

    String getDaysWithInsurerForReview(int id);

    int getSubscriberClaimDays(int id);

    int getSubscriberClaimRejects(int id);
    
    int getFixedFeeClaimDays(int id);

    int getClaimRejects(int id);
    
    String getDaysAwaitingLiabilityResolution(int id);

    void saveClaimWithoutUpdatingLiabilityPayment(Claim claim);

    List getDuplicateSupplementaryInvoiceClaims(String customerClaimRef, int claimId);
    
    Claim getOriginalSupplementaryInvoicedClaim(String customerClaimRef);
    
    int getSubscriberClaimRejectedDays(int claimId);
    
    int getFixedFeeClaimRejectedDays(int claimId);
    
    int updateReservationToTicket(String oldReference, String newReference, Integer choId, String sender);
    
    int updateQueuedTicket(QueuedTicket queuedTicket, Integer choId);
    
    List<QueuedTicket> getQueuedTicket();
 
    Claim getClaimByChoIdAndCHOReferenceNumber(Integer choId, String sClaimReferenceNumber);
    
    Claim updateClaimWithInvalidSessionVersion(Claim claim);
    
    void updateLiabilityPayment(Claim claim);

    int getDaysSinceInvoiceUploadToEscalate(Integer claimId);

    int getNumberOfTimesContestedWithCHOtoEscalate(Integer claimId);
    
    int getNoOfRejectedClaims(Integer reasonOfRejectionId);

    String getOverlappingHire(Claim claim);
    
    int getActivityMonitorRequestInterval();
    
    boolean isEnableActivityMonitor();

    public int createChaseTask(Claim claim);

    public List<Claim> getTotalLossChaseClaims();

    public String stopClaimChase(String choRef);

    public void setTotalLoss(Claim claim, boolean isTotalLoss);
    
    public boolean setLiability(Claim claim, LiabilityStatus liability);
        
    boolean setPenaltyStartToDateInvoiced(String choReference);
    
    Map adjustAutoPenaltyCharge(Claim claim, Date autoPenaltyStart, boolean isCHO);
            
    Map applyPenaltyCharge(Claim claim, Boolean isPenaltyAlertNotUsed, BigDecimal hirePenaltyChargeAmount, 
            String hirePenaltyPercentage, BigDecimal repairPenaltyChargeAmount, String repairPenaltyPercentage);
    
    boolean canShowPenaltyChargeAlert(Claim claim, boolean isCHO);
    
    BigDecimal calculateHirePenaltyChargeVal(Claim claim, String percentage);
    BigDecimal calculateHirePenaltyChargeVal(Claim claim);

    BigDecimal calculateRepairPenaltyChargeVal(Claim claim, String percentage);
    BigDecimal calculateRepairPenaltyChargeVal(Claim claim);

    BigDecimal getHirePenaltyPercentageVal(Claim claim);
    BigDecimal getRepairPenaltyPercentageVal(Claim claim);
    
    boolean addOnHireTask(Claim claim);

    void setInitialPenaltyBand(Claim claim);
}
