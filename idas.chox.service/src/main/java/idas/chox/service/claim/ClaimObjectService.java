package idas.chox.service.claim;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.LiabilityStatus;

/**
 *
 * @author abrar
 */
public class ClaimObjectService {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimObjectService.class);

    private Map<Integer,String> dropDownMapWithNulls;
    private Map<Integer,String> dropDownMap;

    public Map<Integer,String> getLiabilityStatusMap(boolean withNulls) {
        if (!withNulls && dropDownMap == null) {
            dropDownMap = new HashMap();
            LiabilityStatus[] arr = LiabilityStatus.values();
            for (int i = 1; i < arr.length; i++) {
                dropDownMap.put(arr[i].getLiablityValue(), arr[i].toString());
            }
        }
        else if (withNulls && dropDownMapWithNulls == null) {
            dropDownMapWithNulls = new HashMap();
            LiabilityStatus[] arr = LiabilityStatus.values();
            for (int i = 0; i < arr.length; i++) {
                if (i==0) {
                    dropDownMapWithNulls.put(arr[i].getLiablityValue(), "-- Please Select --");
                }
                else {
                    dropDownMapWithNulls.put(arr[i].getLiablityValue(), arr[i].toString());
                }
            }
        }
        return withNulls ? dropDownMapWithNulls: dropDownMap ;
    }


    public Claim cloneClaimForSupplementaryInvoice(Claim claim) {

        Claim newClaim;
        try {
            // Create a shallow copy (see http://commons.apache.org/beanutils/api/org/apache/commons/beanutils/BeanUtilsBean.html)
            newClaim = (Claim) BeanUtils.cloneBean(claim);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            LOG.error("Mapping the old claim to new Supplementary Invoiced claim failed exception message {}", ex.getMessage(), ex);
            return null;
        }

        newClaim.setLiability(claim.getLiabilityStatus());
        newClaim.setLiabilityStatusModifiedDate(claim.getLiabilityStatusModifiedDate());
        newClaim.setLiabilityModifiedDate(claim.getLiabilityModifiedDate());
        newClaim.setLiabilityAgreedDate(claim.getLiabilityAgreedDate());
        newClaim.setHireMonitoringEcds(null);
        newClaim.setInsurerHireMonitoringEcds(null);
        newClaim.setPreviousStatus(null);
        newClaim.setStatus(null);
        newClaim.setId(null);
        newClaim.setCreatedDate(null);
        newClaim.setCreatedBy(null);
        newClaim.setVersion(null);
        newClaim.setChoReference(null);
        newClaim.setVehicleHire(null);
        newClaim.setInsurerVehicleHire(null);
        newClaim.setHireMonitoringDetail(null);
        newClaim.setInsurerHireMonitoringDetail(null);
        if (null == claim.getClaimType()) {
            LOG.error("Error determining type for cloned claim '{}': {}", claim.getChoReference(), claim.getClaimType());
            newClaim.setClaimType(ClaimType.GTA_SUPPLEMENTARY_INVOICE);
        } else switch (claim.getClaimType()) {
            case GTA:
            case GTA_ORIGINAL_INVOICE:
                newClaim.setClaimType(ClaimType.GTA_SUPPLEMENTARY_INVOICE);
                break;
            case INSURER_CLAIM:
            case INSURER_ORIGINAL_INVOICE:
            case INSURER_INVOICE:
                newClaim.setClaimType(ClaimType.INSURER_SUPPLEMENTARY_INVOICE);
                break;
            case INSURER_VS_INSURER:
            case INSURER_VS_INSURER_ORIGINAL_INVOICE:
                newClaim.setClaimType(ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE);
                break;
            case SUBSCRIBER:
            case SUBSCRIBER_ORIGINAL_INVOICE:
                newClaim.setClaimType(ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE);
                break;
            case COLLABORATION_PROTOCOL:
            case COLLABORATION_PROTOCOL_ORIGINAL_INVOICE:
                newClaim.setClaimType(ClaimType.COLLABORATION_PROTOCOL_SUPPLEMENTARY_INVOICE);
                break;
            case FIXED_FEE:
            case FIXED_FEE_ORIGINAL_INVOICE:
                newClaim.setClaimType(ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE);
                break;
            default:
                LOG.error("Error determining type for cloned claim '{}': {}", claim.getChoReference(), claim.getClaimType());
                newClaim.setClaimType(ClaimType.GTA_SUPPLEMENTARY_INVOICE);
                break;
        }
        // Updates for Insurer vs Insurer claims
        newClaim.setClaimOwnerOriginal(null);
        newClaim.setWorkgroupOriginal(null);
        if (claim.getClaimOwnerOriginal() != null) {
            newClaim.setClaimOwner(claim.getClaimOwnerOriginal());
        }
        if (claim.getWorkgroupOriginal() != null) {
            newClaim.setWorkgroup(claim.getWorkgroupOriginal());
        }
        newClaim.setInvoice(null);
        newClaim.setAttachments(null);
        newClaim.setNotifications(null);
        newClaim.setComments(null);
        newClaim.setHistories(null);
        newClaim.setKeoghsRequest(null);
        newClaim.setFraudCheckStatus(0);
        newClaim.setFraudResultAcknowledged(false);
        newClaim.setSentToKeoghs(false);

        return newClaim;

    }
}
