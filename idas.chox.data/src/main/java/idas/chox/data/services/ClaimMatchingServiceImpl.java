package idas.chox.data.services;

import java.util.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimMatchingBand;
import idas.chox.core.model.ClaimMatchingEntry;
import idas.chox.core.model.ClaimMatchingImportEntry;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ClaimMatchingBandService;
import idas.chox.core.services.ClaimMatchingService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author john
 */
public class ClaimMatchingServiceImpl extends SecureDataService implements ClaimMatchingService {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimMatchingServiceImpl.class);
    BreBandService breBandService;
    ClaimMatchingBandService claimMatchingBandService;

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setClaimMatchingBandService(ClaimMatchingBandService claimMatchingBandService) {
        this.claimMatchingBandService = claimMatchingBandService;
    }
    
    @Override
    public List<ClaimMatchingImportEntry> getClaimMatchingImportEntries(String insurerName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ClaimMatchingImportEntry.class);
        criteria.add(Restrictions.eq("insurerName", insurerName));
        return findByCriteria(criteria);
    }

    @Override
    public ClaimMatchingEntry getClaimMatchingEntry(Date incidentDate, String thirdPartyVehicleRegistration) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ClaimMatchingEntry.class);
        criteria.add(Restrictions.eq("thirdPartyVehicleRegistration", thirdPartyVehicleRegistration));
        criteria.add(Restrictions.eq("matchStatus", 0));
        // Match incident date on date only, ignoring time component
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(incidentDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date fromDate = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date toDate = calendar.getTime();

        criteria.add(Restrictions.between("incidentDate", fromDate, toDate));
        
        return (ClaimMatchingEntry)getByCriteria(criteria);
    }
    
    @Override
    public Claim getClaimMatch(Date incidentDate, String thirdPartyVehicleRehistration) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.not(Restrictions.in("claimType", ClaimType.getSupplementaryInvoiceTypes())));
        criteria.createCriteria("customer").add(Restrictions.eq("vehicleRegistration", thirdPartyVehicleRehistration));
        // Match incident date on date only, ignoring time component
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(incidentDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date fromDate = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date toDate = calendar.getTime();

        criteria.createCriteria("incident").add(Restrictions.between("date", fromDate, toDate));
        criteria.add(Restrictions.in("status", Arrays.asList(
                ClaimStatus.CLAIM_PENDING, ClaimStatus.CLAIM_REFERRED_TO_FNOL,
                ClaimStatus.CLAIM_REJECTION_CONTESTED, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED,
                ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED,
                ClaimStatus.CLAIM_UPDATE_BY_ENG)));

        List<Claim> matchedClaims = findByCriteria(criteria);
        
        // Now need to check BRE Band to verify claim matching active for claim type and customer vehicle type, returning first matched claim
        for (Claim c : matchedClaims) {
            c.setBreBand(breBandService.getBreBand(c.getChorganisation().getId(), c.getInsurer().getId()));
            if (c.getBreBand().isClaimMatchingEnable()) {
                ClaimMatchingBand claimMatchingBand = claimMatchingBandService.getClaimMatchingBand(
                    c.getBreBand().getId(),
                    c.getClaimType(), c.getCustomer().getVehicleClass().getName());
                if (claimMatchingBand != null) {
                    return c;
                }
            }
        }
        return (Claim) null;
    }

    @Override
    public ClaimMatchingEntry getClaimMatchingEntry(String claimNumber) {
        ClaimMatchingEntry claim;
        DetachedCriteria criteria = DetachedCriteria.forClass(ClaimMatchingEntry.class);
        criteria.add(Restrictions.eq("claimNumber", claimNumber));
        claim = (ClaimMatchingEntry) getByCriteria(criteria);
        return claim;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void save(ClaimMatchingEntry entry) {
        super.save(entry);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void delete(ClaimMatchingImportEntry entry) {
        super.delete(entry);
    }
    
}
