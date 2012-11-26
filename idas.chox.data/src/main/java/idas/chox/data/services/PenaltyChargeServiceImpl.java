package idas.chox.data.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.PenaltyCharge;
import static idas.chox.core.model.PenaltyCharge.*;
import idas.chox.core.services.PenaltyChargeService;

public class PenaltyChargeServiceImpl extends SecureDataService implements PenaltyChargeService {

    private static final Logger LOG = LoggerFactory.getLogger(PenaltyChargeServiceImpl.class);

    @Override
    public List<PenaltyCharge> getPenaltyCharges(Date hireStart, PenaltyType penaltyType, PenaltyName penaltyName) {

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
         *  e.g If two entries present from the query reuslt then 
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
         *  e.g If two entries present from the query reuslt then 
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
         *  e.g If three entries are returned from the above query result then 
         *  two entries should be removed by looking at the 'Penalty Age' of the Inoice.
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
        Date hireStart = claim.getVehicleHire() != null ? claim.getVehicleHire().getHireStart() : claim.getInvoice().getDateInvoiced();
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
        Date hireStart = claim.getVehicleHire() != null ? claim.getVehicleHire().getHireStart() : claim.getInvoice().getDateInvoiced();
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

        Date hireStart = claim.getVehicleHire() != null ? claim.getVehicleHire().getHireStart() : claim.getInvoice().getDateInvoiced();
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
            Date hireStart = claim.getVehicleHire() != null ? claim.getVehicleHire().getHireStart() : claim.getInvoice().getDateInvoiced();
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
            Date hireStart = claim.getVehicleHire() != null ? claim.getVehicleHire().getHireStart() : claim.getInvoice() != null ? claim.getInvoice().getDateInvoiced() : new Date();
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
            Date hireStart = claim.getVehicleHire() != null ? claim.getVehicleHire().getHireStart() : claim.getInvoice() != null ? claim.getInvoice().getDateInvoiced() : new Date();
            PenaltyType penaltyType = ClaimType.getPenaltyType(claim.getClaimType());
            return getPenaltyCharges(hireStart, penaltyType, PenaltyName.HIRE).get(0).getPenaltyStartAge();
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
            Date hireStart = claim.getVehicleHire() != null ? claim.getVehicleHire().getHireStart() : claim.getInvoice().getDateInvoiced();
            PenaltyType penaltyType = ClaimType.getPenaltyType(claim.getClaimType());
            List<PenaltyCharge> penaltyCharges = getPenaltyCharges(hireStart, penaltyType, PenaltyName.HIRE);
            int size = penaltyCharges.size();
            return penaltyCharges.get(size - 1).getPenaltyStartAge();
        } catch (Exception ex) {
            LOG.error("Exception in getting last available penalty band: ", ex);
            return 0;
        }
    }
}
