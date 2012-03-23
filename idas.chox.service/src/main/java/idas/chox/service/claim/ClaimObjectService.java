package idas.chox.service.claim;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
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

    public Claim cloneClaimForSupplementaryInvoice(Claim claim) {

        Claim newClaim = null;
        try {
            // Create a shallow copy (see http://commons.apache.org/beanutils/api/org/apache/commons/beanutils/BeanUtilsBean.html)
            newClaim = (Claim) BeanUtils.cloneBean(claim);
        } catch (IllegalAccessException ex) {
            LOG.error("Mapping the old claim to new Supplementary Invoiced claim failed exception message {}", ex.getMessage(), ex);
        } catch (InstantiationException ex) {
            LOG.error("Mapping the old claim to new Supplementary Invoiced claim failed exception message {}", ex.getMessage(), ex);
        } catch (InvocationTargetException ex) {
            LOG.error("Mapping the old claim to new Supplementary Invoiced claim failed exception message {}", ex.getMessage(), ex);
        } catch (NoSuchMethodException ex) {
            LOG.error("Mapping the old claim to new Supplementary Invoiced claim failed exception message {}", ex.getMessage(), ex);
        }

        newClaim.setHireMonitoringEcds(null);
        newClaim.setPreviousStatus(null);
        newClaim.setStatus(null);
        newClaim.setId(null);
        newClaim.setCreatedDate(null);
        newClaim.setCreatedBy(null);
        newClaim.setVersion(null);
        newClaim.setChoReference(null);
        newClaim.setVehicleHire(null);
        newClaim.setHireMonitoringDetail(null);
//        newClaim.setOriginalSupplementaryInvoicedClaim(false);
        if (claim.getClaimType() == ClaimType.GTA || claim.getClaimType() == ClaimType.GTA_ORIGINAL_INVOICE) {
            newClaim.setClaimType(ClaimType.GTA_SUPPLEMENTARY_INVOICE);
        }
        else if (claim.getClaimType() == ClaimType.INSURER_VS_INSURER || claim.getClaimType() == ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE) {
            newClaim.setClaimType(ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE);
        }
        else if (claim.getClaimType() == ClaimType.SUBSCRIBER || claim.getClaimType() == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE) {
            newClaim.setClaimType(ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE);
        } else {
            LOG.error("Error determining type for cloned claim '{}': {}", claim.getChoReference(), claim.getClaimType());
            newClaim.setClaimType(ClaimType.GTA_SUPPLEMENTARY_INVOICE);
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
        newClaim.setInvoiceOriginal(null);
        newClaim.setAttachments(null);
        newClaim.setComments(null);
        newClaim.setHistories(null);
        newClaim.setNotifications(null);

        return newClaim;

    }
}
