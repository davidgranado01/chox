/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.claim;

import idas.chox.core.model.Claim;
import idas.chox.core.model.LiabilityStatus;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author abrar
 */
public class ClaimObjectService {

    private Map dropDownMap;
    private Map dropDownMapSearch;
    private static final Logger LOG = LoggerFactory.getLogger(ClaimObjectService.class);

    /**
     * @return the dropDownList
     */
    public Map getLiabilityStatusMap() {
        if (dropDownMap == null) {
            dropDownMap = new HashMap();
            LiabilityStatus[] arr = LiabilityStatus.values();
            for (int i = 0; i < arr.length; i++) {
                dropDownMap.put(arr[i].ordinal(), arr[i]);
            }
        }
        return dropDownMap;
    }

    /**
     * @return the dropDownList
     */
    public Map getLiabilityStatusSearchMap() {
        if (dropDownMapSearch == null) {
            dropDownMapSearch = new HashMap();
            LiabilityStatus[] arr = LiabilityStatus.values();
            for (int i = 1; i < arr.length; i++) {
                dropDownMapSearch.put(arr[i].ordinal(), arr[i]);
            }
        }
        return dropDownMapSearch;
    }

    public Claim mapClaimToNewClaim(Claim claim) {

        Claim newClaim = new Claim();
        try {
            BeanUtils.copyProperties(newClaim, claim);

            newClaim.setId(null);
            newClaim.setCreatedDate(null);
            newClaim.setCreatedBy(null);
            newClaim.setVersion(null);
            newClaim.setChoReference(null);
            newClaim.setInvoice(null);
            newClaim.setInvoice_original(null);
            newClaim.setAttachments(null);
            newClaim.setComments(null);
            newClaim.setPreviousStatus(null);
            newClaim.setHistories(null);
            newClaim.setStatus(null);
            
        } catch (IllegalAccessException ex) {
            LOG.error("Mapping the old claim to new Supplementary Invoiced claim failed exception message {}", ex.getMessage());
        } catch (InvocationTargetException ex) {
            LOG.error("Mapping the old claim to new Supplementary Invoiced claim failed exception message {}", ex.getMessage());
        }



//          newClaim.setStatusModifiedDate(new Date());
//        newClaim.setManagingRepair(claim.getManagingRepair());
//        newClaim.setPolicyHolderContactDate(claim.getPolicyHolderContactDate());
//        newClaim.setChoReference(claim.getChoReference());
//        newClaim.setStatus(claim.getStatus());
//        newClaim.setClaimNumber(claim.getClaimNumber());
//        newClaim.setIndemnityAmount(claim.getIndemnityAmount());
//        newClaim.setPercentageLiabilityAccepted(claim.getPercentageLiabilityAccepted());
//        newClaim.setIsQuantumDispute(claim.getIsQuantumDispute());
//        newClaim.setIsInvoiceReviewRequired(claim.getIsInvoiceReviewRequired());
//        newClaim.setCreditAgreementDate(claim.getCreditAgreementDate());
//        newClaim.setGtaNoticeDate(claim.getGtaNoticeDate());
//        newClaim.setIsFnolReviewed(claim.isIsFnolReviewed());
//        newClaim.setReasonOfRejection(claim.getReasonOfRejection());
//        newClaim.setStatusModifiedDate(claim.getStatusModifiedDate());
//        newClaim.setPreviousStatus(claim.getPreviousStatus());
//        newClaim.setClaimOwner(claim.getClaimOwner());
//        newClaim.setSupplierClaimOwner(claim.getSupplierClaimOwner());
//        newClaim.setBreBand(claim.getBreBand());
//        newClaim.setPercentageLiabilityCho(claim.getPercentageLiabilityCho());
//        newClaim.setLiabilityAgreedDate(claim.getLiabilityAgreedDate());
//        newClaim.setLiabilityStatus(claim.getLiabilityStatus());
//        newClaim.setTpiClaim(claim.isTpiClaim());
//        newClaim.setSpecialRoutedTpiClaim(claim.isSpecialRoutedTpiClaim());
//        newClaim.setTpiClaimStatus(claim.getTpiClaimStatus());
//        
//        newClaim.setInsurer(claim.getInsurer());
//        newClaim.setChorganisation(claim.getChorganisation());
//        newClaim.setCustomer(claim.getCustomer());
//        newClaim.setIncident(claim.getIncident());
//        newClaim.setInvoice(claim.getInvoice());
//        newClaim.setInvoice_original(claim.getInvoice_original());
//        newClaim.setThirdParty(claim.getThirdParty());
//        newClaim.setVehicleHire(claim.getVehicleHire());
//        newClaim.setEngineerReport(claim.getEngineerReport());
//        newClaim.setHireMonitoringDetail(claim.getHireMonitoringDetail());
//        newClaim.setWorkgroup(claim.getWorkgroup());
//        
//        newClaim.setHireMonitoringEcds(claim.getHireMonitoringEcds());
//        newClaim.setNotifications(claim.getNotifications());
//        newClaim.setAttachments(claim.getAttachments());
//        newClaim.setHistories(claim.getHistories());
//        newClaim.setComments(claim.getComments());
        return newClaim;
    }
}
