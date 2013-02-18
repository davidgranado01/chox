package idas.chox.data.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.PenaltyCharge;
import static idas.chox.core.model.PenaltyCharge.*;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerDiscountService;
import idas.chox.core.services.PenaltyChargeService;
import idas.chox.core.services.UserService;
import idas.chox.core.util.DateHelper;

public class PenaltyChargeServiceImpl extends SecureDataService implements PenaltyChargeService {

    private static final Logger LOG = LoggerFactory.getLogger(PenaltyChargeServiceImpl.class);
    
    private ClaimService claimService;
    private UserService userService;
    private InsurerDiscountService insurerDiscountService;
    private BreBandService breBandService;

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setInsurerDiscountService(InsurerDiscountService insurerDiscountService) {
        this.insurerDiscountService = insurerDiscountService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public List<PenaltyCharge> getPenaltyCharges(Date hireStart, PenaltyType penaltyType, PenaltyName penaltyName) {
        LOG.debug("Getting penalty charge for hireStart='{}', PenaltyType='{}', PenaltyName='{}'",
                    new Object[]{hireStart, penaltyType, penaltyName});
        List<PenaltyCharge> penaltyCharges = new ArrayList<PenaltyCharge>();

        // Get all hire penalty charges where penaltyStartDate <= hireStart
        DetachedCriteria criteria = DetachedCriteria.forClass(PenaltyCharge.class, "pc");
        criteria.add(Restrictions.eq("pc.penaltyType", penaltyType));
        criteria.add(Restrictions.le("pc.penaltyStartDate", hireStart));
        if (penaltyName.equals(PenaltyName.HIRE)) {
            criteria.add(Restrictions.isNotNull("pc.hirePenaltyPercentageDsc"));
        } else if (penaltyName.equals(PenaltyName.REPAIR)) {
            criteria.add(Restrictions.isNotNull("pc.repairPenaltyPercentageDsc"));
        }
        criteria.addOrder(Order.asc("pc.penaltyStartAge"));

        /* Subquery to exclude the old entries 
         *  e.g If two entries present from the query then 
         *  one entry should be excluded by looking at 'Penalty Start' date.
         * 
         *   DEFAULT 30 01/01/1950       7.5%  7.5   2.5% 2.5 -- THIS ENTRY SHOULD BE REMOVED IF THE HIRE START IS >= 15/06/2012.
         *   DEFAULT 30 15/06/2012      12.5% 12.5   2.5% 2.5 
         *   DEFAULT 60 01/01/1950        15%   15     5%   5 -- THIS ENTRY SHOULD BE REMOVED IF THE HIRE START IS >= 15/06/2012.
         *   DEFAULT 60 15/06/2012        20%   20     5%   5 
         *   DEFAULT 90 01/01/1950 Commercial    0 <NULL>   0 -- THIS ENTRY SHOULD BE REMOVED IF THE HIRE START IS >= 15/06/2012.
         *   DEFAULT 90 15/06/2012 Commercial    0 <NULL>   0 
         */
        DetachedCriteria subQuery = DetachedCriteria.forClass(PenaltyCharge.class, "pc1");
        subQuery.add(Restrictions.eq("pc1.penaltyType", penaltyType));
        subQuery.add(Restrictions.le("pc1.penaltyStartDate", hireStart));
        if (penaltyName.equals(PenaltyName.HIRE)) {
            subQuery.add(Restrictions.isNotNull("pc1.hirePenaltyPercentageDsc"));
        } else if (penaltyName.equals(PenaltyName.REPAIR)) {
            subQuery.add(Restrictions.isNotNull("pc1.repairPenaltyPercentageDsc"));
        }
        subQuery.add(Restrictions.gtProperty("pc1.penaltyStartDate", "pc.penaltyStartDate"));
        subQuery.add(Restrictions.eqProperty("pc1.penaltyStartAge", "pc.penaltyStartAge"));
        subQuery.setProjection(Projections.id());

        criteria.add(Subqueries.notExists(subQuery));

        try {
            penaltyCharges = findByCriteria(criteria);
        } catch (Exception ex) {
            LOG.error("Exception in executing HIRE penalty percentage query: ", ex);
        }
        
        if (penaltyCharges == null || penaltyCharges.isEmpty()) {
            LOG.error("No penelty charges found for hireStart='{}', PenaltyType='{}', PenaltyName='{}'",
                    new Object[]{hireStart, penaltyType, penaltyName});
        }
        return penaltyCharges;
    }

    @Override
    public PenaltyCharge getPenaltyCharge(Date hireStart, int penaltyAge, PenaltyType penaltyType, PenaltyName penaltyName) {

        PenaltyCharge penaltyCharge = null;

        // Get all hire penalty charges where penaltyStartDate <= hireStart
        DetachedCriteria criteria = DetachedCriteria.forClass(PenaltyCharge.class, "pc");
        criteria.add(Restrictions.eq("pc.penaltyType", penaltyType));
        criteria.add(Restrictions.le("pc.penaltyStartDate", hireStart));
        criteria.add(Restrictions.lt("pc.penaltyStartAge", penaltyAge));
        if (penaltyName.equals(PenaltyName.HIRE)) {
            criteria.add(Restrictions.isNotNull("pc.hirePenaltyPercentageDsc"));
        } else if (penaltyName.equals(PenaltyName.REPAIR)) {
            criteria.add(Restrictions.isNotNull("pc.repairPenaltyPercentageDsc"));
        }
        criteria.addOrder(Order.asc("pc.penaltyStartAge"));

        /* Subquery-1 to exclude the old entries 
         *  e.g If two entries present from the query then 
         *  one entry should be excluded by looking at 'Penalty Start' date.
         * 
         *   DEFAULT 30 01/01/1950       7.5%  7.5   2.5% 2.5 -- THIS ENTRY SHOULD BE REMOVED IF THE HIRE START IS >= 15/06/2012.
         *   DEFAULT 30 15/06/2012      12.5% 12.5   2.5% 2.5 
         *   DEFAULT 60 01/01/1950        15%   15     5%   5 -- THIS ENTRY SHOULD BE REMOVED IF THE HIRE START IS >= 15/06/2012.
         *   DEFAULT 60 15/06/2012        20%   20     5%   5 
         *   DEFAULT 90 01/01/1950 Commercial    0 <NULL>   0 -- THIS ENTRY SHOULD BE REMOVED IF THE HIRE START IS >= 15/06/2012.
         *   DEFAULT 90 15/06/2012 Commercial    0 <NULL>   0 
         */
        DetachedCriteria penaltyStartDateRestrictionSubQuery = DetachedCriteria.forClass(PenaltyCharge.class, "pc1");
        penaltyStartDateRestrictionSubQuery.add(Restrictions.eq("pc1.penaltyType", penaltyType));
        penaltyStartDateRestrictionSubQuery.add(Restrictions.le("pc1.penaltyStartDate", hireStart));
        if (penaltyName.equals(PenaltyName.HIRE)) {
            penaltyStartDateRestrictionSubQuery.add(Restrictions.isNotNull("pc1.hirePenaltyPercentageDsc"));
        } else if (penaltyName.equals(PenaltyName.REPAIR)) {
            penaltyStartDateRestrictionSubQuery.add(Restrictions.isNotNull("pc1.repairPenaltyPercentageDsc"));
        }
        penaltyStartDateRestrictionSubQuery.add(Restrictions.gtProperty("pc1.penaltyStartDate", "pc.penaltyStartDate"));
        penaltyStartDateRestrictionSubQuery.add(Restrictions.eqProperty("pc1.penaltyStartAge", "pc.penaltyStartAge"));
        penaltyStartDateRestrictionSubQuery.setProjection(Projections.id());

        /* SUBQUERY-2 to get 'Penalty Charge' using 'Penalty Age'    
         *  e.g If three entries are returned from the above query then 
         *  two entries should be removed by looking at the 'Penalty Age' of the Invoice.
         * 
         *   DEFAULT 30 15/06/2012      12.5% 12.5   2.5% 2.5 -- THIS ENTRY SHOULD BE REMOVED IF THE INVOICE PENALTY AGE IS > 60 AND <= 90 DAYS.
         *   DEFAULT 60 15/06/2012        20%   20     5%   5 
         *   DEFAULT 90 15/06/2012 Commercial    0 <NULL>   0 -- THIS ENTRY SHOULD BE REMOVED IF THE INVOICE PENALTY AGE IS > 60 AND <= 90 DAYS.
         */
        DetachedCriteria penaltyAgeRestrictionSubQuery = DetachedCriteria.forClass(PenaltyCharge.class, "pc2");
        penaltyAgeRestrictionSubQuery.add(Restrictions.eq("pc2.penaltyType", penaltyType));
        penaltyAgeRestrictionSubQuery.add(Restrictions.lt("pc2.penaltyStartAge", penaltyAge));
        if (penaltyName.equals(PenaltyName.HIRE)) {
            penaltyAgeRestrictionSubQuery.add(Restrictions.isNotNull("pc2.hirePenaltyPercentageDsc"));
        } else if (penaltyName.equals(PenaltyName.REPAIR)) {
            penaltyAgeRestrictionSubQuery.add(Restrictions.isNotNull("pc2.repairPenaltyPercentageDsc"));
        }
        penaltyAgeRestrictionSubQuery.add(Restrictions.gtProperty("pc2.penaltyStartAge", "pc.penaltyStartAge"));
        penaltyAgeRestrictionSubQuery.setProjection(Projections.id());

        criteria.add(Subqueries.notExists(penaltyStartDateRestrictionSubQuery));
        criteria.add(Subqueries.notExists(penaltyAgeRestrictionSubQuery));

        try {
            List<PenaltyCharge> penaltyCharges = findByCriteria(criteria);
            if (penaltyCharges.size() > 0) {
                penaltyCharge = penaltyCharges.get(0);
                if (penaltyCharges.size() > 1) {
                    LOG.error("More than 1 'Penalty Charge' returned from the result. returned size is: {}", penaltyCharges.size());
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception in executing HIRE penalty percentage query: ", ex);
        }
        return penaltyCharge;
    }

    /*
     *  Returns the String value of the penalty percentage. 
     */
    @Override
    public String getPenaltyPercentageDsc(Claim claim, PenaltyName penaltyName) {

        Invoice inv = claim.getInvoice();
        Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                    : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
        PenaltyType penaltyType = ClaimType.getPenaltyType(claim.getClaimType());

        if ((penaltyName.equals(PenaltyName.HIRE) && inv.getHireNet().compareTo(BigDecimal.ZERO) == 1) 
                || (penaltyName.equals(PenaltyName.REPAIR) && inv.getRepairNet().compareTo(BigDecimal.ZERO) == 1)) {

            int dateDiff = inv.getInvoicedDays();
            PenaltyCharge penaltyCharge = getPenaltyCharge(hireStart, dateDiff, penaltyType, penaltyName);

            if (penaltyCharge != null && penaltyName.equals(PenaltyName.HIRE)) {
                return penaltyCharge.getHirePenaltyPercentageDsc();
            } else if (penaltyCharge != null && penaltyName.equals(PenaltyName.REPAIR)) {
                return penaltyCharge.getRepairPenaltyPercentageDsc();
            }
        }
        return "0%";
    }

    /*
     *  Returns the BigDecimal value for the mapped string type penalty percentage.
     */
    @Override
    public BigDecimal getPenaltyPercentageVal(Claim claim, PenaltyName penaltyName) {

        Invoice inv = claim.getInvoice();
        Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                    : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
        PenaltyType penaltyType = ClaimType.getPenaltyType(claim.getClaimType());

        if ((penaltyName.equals(PenaltyName.HIRE) && inv.getHireNet().compareTo(BigDecimal.ZERO) == 1) 
                || (penaltyName.equals(PenaltyName.REPAIR) && inv.getRepairNet().compareTo(BigDecimal.ZERO) == 1)) {

            int dateDiff = inv.getInvoicedDays();
            PenaltyCharge penaltyCharge = getPenaltyCharge(hireStart, dateDiff, penaltyType, penaltyName);

            if (penaltyCharge != null && penaltyName.equals(PenaltyName.HIRE)) {
                return penaltyCharge.getHirePenaltyPercentageVal();
            } else if (penaltyCharge != null && penaltyName.equals(PenaltyName.REPAIR)) {
                return penaltyCharge.getRepairPenaltyPercentageVal();
            }
        }
        return BigDecimal.ZERO.setScale(2);
    }

    /*
     *  Calculate the penalty amount for the given claim.
     */
    @Override
    public BigDecimal calculatePenaltyChargeVal(Claim claim, PenaltyName penaltyName) {

        if (penaltyName.equals(PenaltyName.HIRE)) {
            return (getPenaltyPercentageVal(claim, penaltyName).divide(new BigDecimal(100)).multiply(claim.getInvoice().getHireGross()))
                    .setScale(2, RoundingMode.HALF_UP);
        } else if (penaltyName.equals(PenaltyName.REPAIR)) {
            return (getPenaltyPercentageVal(claim, penaltyName).divide(new BigDecimal(100)).multiply(claim.getInvoice().getRepairGross()))
                    .setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO.setScale(2);
    }

    /*
     *  Calculate the penalty amount using the provided penalty percentage for the given claim.
     */
    @Override
    public BigDecimal calculatePenaltyChargeVal(Claim claim, String Percentage, PenaltyName penaltyName) {

        Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                    : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
        PenaltyType penaltyType = ClaimType.getPenaltyType(claim.getClaimType());

        if (penaltyName.equals(PenaltyName.HIRE)) {
            for (PenaltyCharge hirePenaltyCharge : getPenaltyCharges(hireStart, penaltyType, penaltyName)) {
                if (hirePenaltyCharge.getHirePenaltyPercentageDsc().equals(Percentage)) {
                    return (hirePenaltyCharge.getHirePenaltyPercentageVal().divide(new BigDecimal(100)).multiply(claim.getInvoice().getHireGross()))
                            .setScale(2, RoundingMode.HALF_UP);
                }
            }
        } else if (penaltyName.equals(PenaltyName.REPAIR)) {
            for (PenaltyCharge repairPenaltyCharge : getPenaltyCharges(hireStart, penaltyType, penaltyName)) {
                if (repairPenaltyCharge.getRepairPenaltyPercentageDsc().equals(Percentage)) {
                    return (repairPenaltyCharge.getRepairPenaltyPercentageVal().divide(new BigDecimal(100)).multiply(claim.getInvoice().getRepairGross()))
                            .setScale(2, RoundingMode.HALF_UP);
                }
            }
        }
        return BigDecimal.ZERO.setScale(2);
    }

    /*
     *  Calculate the current penalty band by looking at the age of the invoice.
     */
    @Override
    public int calculateCurrentPenaltyBand(Claim claim) {
        try {
            Invoice inv = claim.getInvoice();
            Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                    : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
            PenaltyType penaltyType = ClaimType.getPenaltyType(claim.getClaimType());
            int dateDiff = inv.getInvoicedDays();
            PenaltyCharge penaltyCharge = getPenaltyCharge(hireStart, dateDiff, penaltyType, PenaltyName.HIRE);
            if (penaltyCharge != null) {
                return penaltyCharge.getPenaltyStartAge();
            } else {
                return 0;
            }
        } catch (Exception ex) {
            LOG.error("Exception in getting current penalty band: ", ex);
            return 0;
        }

    }

    /*
     *  Get the next penalty band. 
     */
    @Override
    public int getNextPenaltyBand(Claim claim) {
        try {
            Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                    : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
            PenaltyType penaltyType = ClaimType.getPenaltyType(claim.getClaimType());
            List<PenaltyCharge> penaltyCharges = getPenaltyCharges(hireStart, penaltyType, PenaltyName.HIRE);
            Invoice inv = claim.getInvoice();
            int dateDiff = 0;
            if (inv != null) {
                dateDiff = inv.getInvoicedDays();
            }
            PenaltyCharge penaltyCharge = getPenaltyCharge(hireStart, dateDiff, penaltyType, PenaltyName.HIRE);
            if (penaltyCharge != null) {
                for (PenaltyCharge charge : penaltyCharges) {
                    if (charge.getPenaltyStartAge() > penaltyCharge.getPenaltyStartAge()) {
                        return charge.getPenaltyStartAge();
                    }
                }
                return getLastPenaltyBand(claim);
            }
            return getFirstPenaltyBand(claim);
        } catch (Exception ex) {
            LOG.error("Exception in getting next penalty band: ", ex);
            return 0;
        }
    }

    /*
     *  Returns the first penalty band for the given claim.
     */
    @Override
    public int getFirstPenaltyBand(Claim claim) {
        try {
            Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                    : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
            PenaltyType penaltyType = ClaimType.getPenaltyType(claim.getClaimType());
            List<PenaltyCharge> penaltyCharges =  getPenaltyCharges(hireStart, penaltyType, PenaltyName.HIRE);
            return (penaltyCharges == null || penaltyCharges.isEmpty()) ? 0 : penaltyCharges.get(0).getPenaltyStartAge();
        } catch (Exception ex) {
            LOG.error("Exception in getting first available penalty band: ", ex);
            return 0;
        }
    }

    /*
     *  Returns the last penalty band for the given claim.
     */
    @Override
    public int getLastPenaltyBand(Claim claim) {
        try {
            Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                    : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
            PenaltyType penaltyType = ClaimType.getPenaltyType(claim.getClaimType());
            List<PenaltyCharge> penaltyCharges = getPenaltyCharges(hireStart, penaltyType, PenaltyName.HIRE);
            return (penaltyCharges == null || penaltyCharges.isEmpty()) ? 0 : penaltyCharges.get(penaltyCharges.size() - 1).getPenaltyStartAge();
        } catch (Exception ex) {
            LOG.error("Exception in getting last available penalty band: ", ex);
            return 0;
        }
    }

//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private boolean updateAutomaticPenaltyCharge(Claim claim) {
        LOG.debug("Updating penalty charges: claim.isAutoPenaltyChargeEnabled()={}, claim.getChorganisation().isAutoPenaltyChargeEnabled()={}, "
                + "!ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus())={}, claim.getInvoice()={}",
                new Object[]{claim.isAutoPenaltyChargeEnabled(), claim.getChorganisation().isAutoPenaltyChargeEnabled(),
                    !ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus()),
                    claim.getInvoice()});

        if (claim.isAutoPenaltyChargeEnabled()
                && claim.getChorganisation().isAutoPenaltyChargeEnabled()
                && !ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus())
                && claim.getInvoice() != null
                && claim.getInvoice().getInvoicedDays() > getFirstPenaltyBand(claim)) {
//                && claim.getInvoice().getPenaltyAlertQty() < calculatePenaltyAlertQty(claim.getInvoice())) {

            try {
                LOG.debug("Calling stored procedure to update penalty charges...");
                callApplyAutoPenaltyCharge(999, claim.getId());
                // The Claim / Invoice may have been modified in the above call.
                // We therefore need to clear these objects from the cache
                // First clear the query/session cache
                evict(claim.getInvoice());
                evict(claim);
                // Then the second-level cache (if activated)
                getCurrentSession().getSessionFactory().evict(Claim.class, claim.getId());
                getCurrentSession().getSessionFactory().evict(Invoice.class, claim.getInvoice().getId());
                LOG.debug("Auto penalty charge applied to claim: {}", claim.getChoReference());
//                applyInsurerDiscounts(claim, userService.findByUserName("system"), true);
//                claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
                return true;
            } catch (Exception ex) {
                LOG.error("Exception thrown while updating auto penalty charge store procedure for claim '{}'", claim.getChoReference(), ex);
                return false;
            }
        }
        return false;
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private void updatePenaltyStartDate(Claim claim, Date autoPenaltyStart) {

        Invoice inv = claim.getInvoice();
        inv.setFullTotalToPay(inv.getFullTotalToPay().subtract(inv.getHirePenaltyCharge()).subtract(inv.getRepairPenaltyCharge()));
        inv.setAutoPenaltyStart(autoPenaltyStart);
        inv.setHirePenaltyPercentage(null);
        inv.setRepairPenaltyPercentage(null);
        inv.setHirePenaltyCharge(BigDecimal.ZERO);
        inv.setRepairPenaltyCharge(BigDecimal.ZERO);
        inv.setPenaltyBand(getFirstPenaltyBand(claim));
        inv.setHirePenaltyChargeAppliedDate(null);
        inv.setRepairPenaltyChargeAppliedDate(null);
        if (inv.getTotalPenaltyCharge() != null && inv.getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) > 0) {
            Comment comment = Comment.New(0, "Penalty charges have been removed from the invoice as the date from which penalty charges are calculated has been manually updated.");
            claim.addComment(comment);
        }
        inv.setTotalPenaltyCharge(BigDecimal.ZERO);
        insurerDiscountService.applyInsurerDiscounts(claim, userService.findByUserName("system"), true);
        claimService.updateClaim(claim);
    }
        
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean setPenaltyStartToDateInvoiced(String choReference) {
        Claim claim = claimService.getClaimByCHOReferenceNumber(choReference);
        if (claim != null && claim.getInvoice() != null) {
            updatePenaltyStartDate(claim, claim.getInvoice().getDateInvoiced());
            updateAutomaticPenaltyCharge(claim);
            insurerDiscountService.applyInsurerDiscounts(claim, null, false);
            return true;
        }

        return false;
    }
    
    @Override
    public Map applyPenaltyCharge(Claim claim, Boolean isPenaltyAlertNotUsed, BigDecimal hirePenaltyChargeAmount,
            String hirePenaltyPercentage, BigDecimal repairPenaltyChargeAmount, String repairPenaltyPercentage) {
        Map resultMap = new HashMap();
        try {

            Invoice invoice = claim.getInvoice();
            BigDecimal newTotalAmountToPay = invoice.getFullTotalToPay().subtract(invoice.getHirePenaltyCharge())
                    .subtract(invoice.getRepairPenaltyCharge()).add(hirePenaltyChargeAmount).add(repairPenaltyChargeAmount);
            if (hirePenaltyChargeAmount.compareTo(BigDecimal.ZERO) > 0 && (hirePenaltyPercentage == null || hirePenaltyPercentage.length() == 0)) {
                resultMap.put("error", "You must supply a value for 'Hire Penalty Percentage'.");
                return resultMap;
            }
            if (repairPenaltyChargeAmount.compareTo(BigDecimal.ZERO) > 0 && (repairPenaltyPercentage == null || repairPenaltyPercentage.length() == 0)) {
                resultMap.put("error", "You must supply a value for 'Repair Penalty Percentage'.");
                return resultMap;
            }
            if (hirePenaltyChargeAmount.compareTo(invoice.getHirePenaltyCharge()) != 0) {
                invoice.setHirePenaltyChargeAppliedDate(DateHelper.getCurrentDateTime());
            }
            if (repairPenaltyChargeAmount.compareTo(invoice.getRepairPenaltyCharge()) != 0) {
                invoice.setRepairPenaltyChargeAppliedDate(DateHelper.getCurrentDateTime());
            }
            invoice.setFullTotalToPay(newTotalAmountToPay);
            invoice.setHirePenaltyCharge(hirePenaltyChargeAmount);
            invoice.setHirePenaltyPercentage(hirePenaltyPercentage);
            invoice.setRepairPenaltyCharge(repairPenaltyChargeAmount);
            invoice.setRepairPenaltyPercentage(repairPenaltyPercentage);
//            totalPenaltyChargeAmount = hirePenaltyChargeAmount.add(repairPenaltyChargeAmount);
            invoice.setTotalPenaltyCharge(hirePenaltyChargeAmount.add(repairPenaltyChargeAmount));

            insurerDiscountService.applyInsurerDiscounts(claim, userService.findByUserName("system"), true);

            if ((isPenaltyAlertNotUsed != null && isPenaltyAlertNotUsed) 
                    || (claim.getChorganisation().isAutoPenaltyChargeEnabled() && claim.isAutoPenaltyChargeEnabled())) {
                int penaltyBand = calculateCurrentPenaltyBand(claim);
                int lastPenaltyBand = getLastPenaltyBand(claim);
                int nextPenaltyBand = getNextPenaltyBand(claim);
                invoice.setPenaltyBand(penaltyBand >= lastPenaltyBand ? -1 : nextPenaltyBand);
            }
            LOG.debug("Hire penalty %: '{}', Repair penalty %: '{}'", hirePenaltyPercentage, repairPenaltyPercentage);
            claimService.updateClaim(claim);

        } catch (Exception ex) {
            LOG.error("Exception thrown applying penalty charges to claim '{}': ", claim.getChoReference(), ex);
            resultMap.put("error", "An internal error occurred applying penalty charges to this claim. Please contact CHOX support.");
        }

        return resultMap;
    }

    @Override
    public Map adjustAutoPenaltyCharge(Claim claim, Date autoPenaltyStart) {
        Map resultMap = new HashMap();
        if (autoPenaltyStart != null) {

            Date invoiceCreationDate = claim.getInvoice().getCreatedDate();
            Date penaltyStartDate = claim.getInvoice().getAutoPenaltyStart();
            // Set both times to 00:00:00
            if (invoiceCreationDate != null) {
                invoiceCreationDate = DateHelper.setStartOfDay(invoiceCreationDate);
            }
            if (penaltyStartDate != null) {
                penaltyStartDate = DateHelper.setStartOfDay(penaltyStartDate);
            }
            // For CHO, the autoPenaltyStartDate must be AFTER the invoice creation date
            LOG.debug("autoPenaltyStart={}, penaltyStartDate={}, invoiceCreationDate={}", new Object[]{autoPenaltyStart, penaltyStartDate, invoiceCreationDate});
            if (autoPenaltyStart.compareTo(penaltyStartDate) != 0 && autoPenaltyStart.compareTo(invoiceCreationDate) < 0) {
                LOG.warn("Attempt (by CHO) to set penalty-start date ({}) to before invoice upload date ({}).", autoPenaltyStart, invoiceCreationDate);
                resultMap.put("error", "The 'Penalty Charge Start Date' cannot be set to before the invoice was uploaded and has not been saved.");
//                this.setActionError("The 'Penalty Charge Start Date' cannot be set to before the invoice was uploaded and has not been saved.");
//                setActionResult("The 'Penalty Charge Calculation Date' cannot be set to before the invoice was uploaded. Your changes have not been saved.");
                return resultMap;
            }
            claimService.updateClaim(claim);
            if (autoPenaltyStart.compareTo(penaltyStartDate) != 0) {
                // The date has been changed
                updatePenaltyStartDate(claim, autoPenaltyStart);
            }
            if (updateAutomaticPenaltyCharge(claim)) {
                LOG.debug("Auto Penalty charges updated for claim '{}'", claim.getChoReference());
                // Invoice details may have changed  so we need to reload the claim
                claim = claimService.getClaim(claim.getId());
                resultMap.put("claim", claim);
            } else {
                LOG.debug("Auto Penalty charges not updated for claim '{}'", claim.getChoReference());
            }
        }

        return resultMap;
    }
    
    @Override
    public boolean canShowPenaltyChargeAlert(Claim claim, boolean isCHO) {
        boolean result = false;
        boolean allowPenaltyCharges = true;
        if (isCHO) {
            Invoice invoice = claim.getInvoice();
            // Set Claim BRE band
            BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
            claim.setBreBand(choBand);
            if (claim.getBreBand() == null) {
                LOG.error("No BRE Band for claim '{}'", claim.getChoReference());
            } else if (!claim.getBreBand().isAllowPenaltyCharges(claim.getClaimType())) {
                allowPenaltyCharges = false;
            }
            if (allowPenaltyCharges && invoice != null
                    && !ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus())
                    && invoice.getPenaltyBand() > -1
                    && (!claim.getChorganisation().isAutoPenaltyChargeEnabled()
                    || (claim.getChorganisation().isAutoPenaltyChargeEnabled()
                    && (!claim.isAutoPenaltyChargeEnabled()
                    || calculateCurrentPenaltyBand(claim) >= getLastPenaltyBand(claim))))) {
                result = invoice.getInvoicedDays() > invoice.getPenaltyBand();
            }
        }
        return result;
    }
}
