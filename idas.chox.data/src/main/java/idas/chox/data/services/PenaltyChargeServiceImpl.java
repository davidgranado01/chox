
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
    public List<PenaltyCharge> getHirePenaltyPercentages(Date hireStart, PenaltyType penaltyType) {

        
        List<PenaltyCharge> hirePenalties = new ArrayList<PenaltyCharge>();
       
        // Get all hire penalty charges where penaltyStartDate <= hireStart
        DetachedCriteria criteria = DetachedCriteria.forClass(PenaltyCharge.class, "pc");
        criteria.add(Restrictions.eq("pc.penaltyType", penaltyType));
        criteria.add(Restrictions.eq("pc.penaltyName", PenaltyName.HIRE));
        criteria.add(Restrictions.le("pc.penaltyStartDate", hireStart));
        criteria.addOrder(Order.asc("pc.penaltyStartAgeFrom"));
        
        /* Subquery to exclude the old entries 
         *  e.g If two entries present from the query reuslt then 
         *  one entry should be excluded by looking at 'Penalty Start' date.
         * 
         *   HIRE DEFAULT 30  7.5  7.5% 1/1/2010  -- THIS OLD ENTRY SHOULD BE excluded.
         *   HIRE DEFAULT 30 12.5 12.5% 1/1/2011
         */
        DetachedCriteria subQuery = DetachedCriteria.forClass(PenaltyCharge.class, "pc1");
        subQuery.add(Restrictions.eq("pc1.penaltyType", penaltyType));
        subQuery.add(Restrictions.eq("pc1.penaltyName", PenaltyName.HIRE));
        subQuery.add(Restrictions.le("pc1.penaltyStartDate", hireStart));
        subQuery.add(Restrictions.gtProperty("pc1.penaltyStartDate", "pc.penaltyStartDate"));
        subQuery.add(Restrictions.eqProperty("pc1.penaltyStartAgeFrom", "pc.penaltyStartAgeFrom"));
        subQuery.setProjection(Projections.id());
        
        criteria.add(Subqueries.notExists(subQuery));
        
        try {
            hirePenalties = findByCriteria(criteria);
        } catch (Exception ex) {
            LOG.error("Exception in executing HIRE penalty percentage query: ",ex);
        }
        return hirePenalties;
    }

    @Override
    public List<PenaltyCharge> getRepairPenaltyPercentages(PenaltyType penaltyType) {
        
        List<PenaltyCharge> repairPenalties = new ArrayList<PenaltyCharge>();
        
        DetachedCriteria criteria = DetachedCriteria.forClass(PenaltyCharge.class, "pc");
        criteria.add(Restrictions.eq("pc.penaltyType", penaltyType));
        criteria.add(Restrictions.eq("pc.penaltyName", PenaltyName.REPAIR));
        criteria.addOrder(Order.asc("pc.penaltyStartAgeFrom"));

        /* Subquery to exclude the old entries 
         *  e.g If two entries present from the query reuslt then 
         *  one entry should be excluded by looking at 'Penalty Start' date.
         * 
         *   REPAIR DEFAULT 30  7.5  7.5% 1/1/2010  -- THIS OLD ENTRY SHOULD BE excluded.
         *   REPAIR DEFAULT 30 12.5 12.5% 1/1/2011
         */
        DetachedCriteria subQuery = DetachedCriteria.forClass(PenaltyCharge.class, "pc1");
        subQuery.add(Restrictions.eq("pc1.penaltyType", penaltyType));
        subQuery.add(Restrictions.eq("pc1.penaltyName", PenaltyName.REPAIR));
        subQuery.add(Restrictions.gtProperty("pc1.penaltyStartDate", "pc.penaltyStartDate"));
        subQuery.add(Restrictions.eqProperty("pc1.penaltyStartAgeFrom", "pc.penaltyStartAgeFrom"));
        subQuery.setProjection(Projections.id());
        
        criteria.add(Subqueries.notExists(subQuery));
        
        try {
            repairPenalties = findByCriteria(criteria);
        } catch (Exception ex) {
            LOG.error("Exception in executing REPAIR penalty percentage query: ", ex);
        }
        return repairPenalties;
    }

    @Override
    public String getHirePenaltyPercentage(Date hireStart, Invoice inv, PenaltyType penaltyType) {

        String penaltyPercenDec = "0%";

        if (inv.getHireNet().compareTo(BigDecimal.ZERO) == 1) {

            long dateDiff = inv.getInvoicedDays();
            List<PenaltyCharge> hirePenalties = getHirePenaltyPercentages(hireStart, penaltyType);
            // the returned HirePenalties should be ordered ascendingly. 
            for (PenaltyCharge penaltyCharge : hirePenalties) {
                if (penaltyCharge.getPenaltyStartAgeFrom() <= dateDiff) {
                    penaltyPercenDec = penaltyCharge.getPenaltyPercentageDsc();
                }
            }
        }
        return penaltyPercenDec;
    }

    @Override
    public String getRepairPenaltyPercentage(Invoice inv, PenaltyType penaltyType) {

        String penaltyPercenDec = "0%";

        if (inv.getRepairNet().compareTo(BigDecimal.ZERO) == 1) {

            long dateDiff = inv.getInvoicedDays();
            List<PenaltyCharge> repairPenalties = getRepairPenaltyPercentages(penaltyType);
            // the returned RepairPenalties should be ordered ascendingly. 
            for (PenaltyCharge penaltyCharge : repairPenalties) {
                if (penaltyCharge.getPenaltyStartAgeFrom() <= dateDiff) {
                    penaltyPercenDec = penaltyCharge.getPenaltyPercentageDsc();
                }
            }
        }
        return penaltyPercenDec;
    }
    
    @Override
    public BigDecimal calculateHirePenaltyCharge(Invoice inv, String hirePercentage, Date hireStart, PenaltyType penaltyType) {

        BigDecimal hirePenaltyAmout = BigDecimal.ZERO.setScale(2);
//        BigDecimal hireNet = claim.getInvoice().getHireNet();
        BigDecimal hireGross = inv.getHireGross();
//        Date hireStart = claim.getVehicleHire() != null ? claim.getVehicleHire().getHireStart() : claim.getInvoice().getDateInvoiced();

        for (PenaltyCharge hirePenaltyPercentageValue : getHirePenaltyPercentages(hireStart, penaltyType)) {
            if (hirePenaltyPercentageValue.getPenaltyPercentageDsc().equals(hirePercentage)) {
//                BigDecimal hirePenaltyWithoutVat = hirePenaltyPercentageValue.getPercentageValue().divide(new BigDecimal(100)).multiply(hireNet);
//                hirePenaltyAmout = hirePenaltyWithoutVat.add(hirePenaltyWithoutVat.multiply(CalcHelper.VAT_RATE)).setScale(2, RoundingMode.HALF_UP);
                hirePenaltyAmout = (hirePenaltyPercentageValue.getPenaltyPercentage().divide(new BigDecimal(100)).multiply(hireGross)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        LOG.debug("calculated HirePenalty Charge = {}", hirePenaltyAmout);
        return hirePenaltyAmout;
    }

    @Override
    public BigDecimal calculateHirePenaltyCharge(Claim claim) {

        Invoice inv = claim.getInvoice();
        BigDecimal hirePenaltyAmout = BigDecimal.ZERO.setScale(2);
        BigDecimal hireGross = inv.getHireGross();
        Date hireStart = claim.getVehicleHire() != null ? claim.getVehicleHire().getHireStart() : claim.getInvoice().getDateInvoiced();
        String calculatedHirePenaltyPercentage = getHirePenaltyPercentage(hireStart, inv, ClaimType.getPenaltyType(claim.getClaimType()));
        for (PenaltyCharge hirePenaltyPercentageValue : getHirePenaltyPercentages(hireStart, ClaimType.getPenaltyType(claim.getClaimType()))) {
            if (hirePenaltyPercentageValue.getPenaltyPercentageDsc().equals(calculatedHirePenaltyPercentage)) {
                hirePenaltyAmout = (hirePenaltyPercentageValue.getPenaltyPercentage().divide(new BigDecimal(100)).multiply(hireGross)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return hirePenaltyAmout;
    }

    
    
    @Override
    public BigDecimal calculateRepairPenaltyCharge(Invoice inv, String repairPercentage, PenaltyType penaltyType) {

        BigDecimal repairPenaltyAmout = BigDecimal.ZERO.setScale(2);
//        BigDecimal repairNet = claim.getInvoice().getRepairNet();
        BigDecimal repairGross = inv.getRepairGross();

        for (PenaltyCharge repairPenaltyPercentage : getRepairPenaltyPercentages(penaltyType)) {
            if (repairPenaltyPercentage.getPenaltyPercentageDsc().equals(repairPercentage)) {
//                BigDecimal repairPenaltyWithoutVat = repairPenaltyPercentage.getPercentageValue().divide(new BigDecimal(100)).multiply(repairNet);
//                repairPenaltyAmout = repairPenaltyWithoutVat.add(repairPenaltyWithoutVat.multiply(CalcHelper.VAT_RATE)).setScale(2, RoundingMode.HALF_UP);
                repairPenaltyAmout = (repairPenaltyPercentage.getPenaltyPercentage().divide(new BigDecimal(100)).multiply(repairGross)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        LOG.debug("calculated RepairPenalty Charge = {}", repairPenaltyAmout);
        return repairPenaltyAmout;
    }
      
    @Override
    public BigDecimal calculateRepairPenaltyCharge(Claim claim) {

        Invoice inv = claim.getInvoice();
        BigDecimal repairPenaltyAmout = BigDecimal.ZERO.setScale(2);
        BigDecimal repairGross = inv.getRepairGross();
        String calculatedRepairPenaltyPercentage = getRepairPenaltyPercentage(inv, ClaimType.getPenaltyType(claim.getClaimType()));
        for (PenaltyCharge repairPenaltyPercentage : getRepairPenaltyPercentages(ClaimType.getPenaltyType(claim.getClaimType()))) {
            if (repairPenaltyPercentage.getPenaltyPercentageDsc().equals(calculatedRepairPenaltyPercentage)) {
                repairPenaltyAmout = (repairPenaltyPercentage.getPenaltyPercentage().divide(new BigDecimal(100)).multiply(repairGross)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return repairPenaltyAmout;
    }
    
}
