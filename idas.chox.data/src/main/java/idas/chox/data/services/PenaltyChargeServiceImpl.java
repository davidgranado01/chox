
package idas.chox.data.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.PenaltyCharge;
import idas.chox.core.model.PenaltyName;
import idas.chox.core.services.PenaltyChargeService;


public class PenaltyChargeServiceImpl extends SecureDataService implements PenaltyChargeService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceServiceImpl.class);
    
    @Override
    public List<PenaltyCharge> getHirePenaltyPercentages(Date hireStart, int penaltyType) {

        
        List<PenaltyCharge> hirePenalties = new ArrayList<PenaltyCharge>();
        try {
            // Get all hire penalty charges where penaltyStartDate < hireStart
            DetachedCriteria criteria = DetachedCriteria.forClass(PenaltyCharge.class);
            criteria.add(Restrictions.eq("penaltyType", penaltyType));
            criteria.add(Restrictions.le("penaltyStartDate", hireStart));
//            criteria.add(Restrictions.eq("penaltyName", PenaltyName.HIRE.getPenaltyNameType()));
            criteria.addOrder(Order.asc("penaltyStartAgeFrom"));


            List<PenaltyCharge> hirePenaltyCharges = findByCriteria(criteria);

            /*
             * copy results into another list which will be returned to the
             * caller after removing old entries.
             */
            for (PenaltyCharge charge : hirePenaltyCharges) {
                hirePenalties.add(charge);
            }

            /*
             * remove old entries from the list.
             */
            for (PenaltyCharge p1 : hirePenaltyCharges) {
                for (PenaltyCharge p2 : hirePenaltyCharges) {
                    if ((p1.getPenaltyStartAgeFrom() == p2.getPenaltyStartAgeFrom())
                            && (p1.getPenaltyStartDate().compareTo(p2.getPenaltyStartDate()) > 0)) {
                        hirePenalties.remove(p2);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown in penalty charge :",ex);
        }


        return hirePenalties;
    }

    @Override
    public List<PenaltyCharge> getRepairPenaltyPercentages(int penaltyType) {
        
        List<PenaltyCharge> repairPenaltyCharges = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(PenaltyCharge.class);
        criteria.add(Restrictions.eq("penaltyType", penaltyType));
//        criteria.add(Restrictions.eq("penaltyName", PenaltyName.REPAIR.getPenaltyNameType()));
        criteria.addOrder(Order.asc("penaltyStartAgeFrom"));
        
        try {
             repairPenaltyCharges = findByCriteria(criteria);
        } catch (Exception ex) {
            LOG.error("Exception thrown in penalty charge :", ex);
        }

        return repairPenaltyCharges;
    }

    @Override
    public String getHirePenaltyPercentage(Date hireStart, Invoice inv, int penaltyType) {

        if (inv.getHireNet().compareTo(BigDecimal.ZERO) == 1) {

            long dateDiff = inv.getInvoicedDays();
            List<PenaltyCharge> hirePenalties = getHirePenaltyPercentages(hireStart, penaltyType);

            for (PenaltyCharge penaltyCharge : hirePenalties) {
                if ((penaltyCharge.getPenaltyStartAgeFrom() <= dateDiff)
                        && ((penaltyCharge.getPenaltyStartAgeTo() > dateDiff)
                            || (penaltyCharge.getPenaltyStartAgeTo() == 0))) {
                    return penaltyCharge.getPenaltyPercentageDsc();
                }
            }

        }
        return PenaltyCharge.ZeroPenaltyPercentage.ZERO_PERCENTAGE.getPercentageDsc();
    }

    @Override
    public String getRepairPenaltyPercentage(Invoice inv, int penaltyType) {

        if (inv.getRepairNet().compareTo(BigDecimal.ZERO) == 1) {

            long dateDiff = inv.getInvoicedDays();
            List<PenaltyCharge> repairPenalties = getRepairPenaltyPercentages(penaltyType);

            for (PenaltyCharge penaltyCharge : repairPenalties) {
                if ((penaltyCharge.getPenaltyStartAgeFrom() <= dateDiff)
                        && ((penaltyCharge.getPenaltyStartAgeTo() > dateDiff)
                            || (penaltyCharge.getPenaltyStartAgeTo() == 0))) {
                    return penaltyCharge.getPenaltyPercentageDsc();
                }
            }
        }
        return PenaltyCharge.ZeroPenaltyPercentage.ZERO_PERCENTAGE.getPercentageDsc();
    }
    
    @Override
    public boolean isAppliedHirePenaltyPercentageDifferent(Date hireStart, Invoice inv, int penaltyType) {

        BigDecimal appliedHirePenaltyPercentageValue = inv.getHirePenaltyPercentageAppliedValue();
        if (appliedHirePenaltyPercentageValue != null) {
            for (PenaltyCharge hirePenaltyPerc : getHirePenaltyPercentages(hireStart, penaltyType)) {
                if (hirePenaltyPerc.getPenaltyPercentageDsc().equals(inv.getHirePenaltyPercentage())) {
                    if (hirePenaltyPerc.getPenaltyPercentage().compareTo(appliedHirePenaltyPercentageValue) != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isAppliedRepairPenaltyPercentageDifferent(Invoice inv, int penaltyType) {

        BigDecimal appliedRepairPenaltyPercentageValue = inv.getRepairPenaltyPercentageAppliedValue();
        if (appliedRepairPenaltyPercentageValue != null) {
            for (PenaltyCharge repairPenaltyPerc : getRepairPenaltyPercentages(penaltyType)) {
                if (repairPenaltyPerc.getPenaltyPercentageDsc().equals(inv.getRepairPenaltyPercentage())) {
                    if (repairPenaltyPerc.getPenaltyPercentage().compareTo(appliedRepairPenaltyPercentageValue) != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public BigDecimal calculateHirePenaltyCharge(Invoice inv, String hirePercentage, Date hireStart, int penaltyType) {

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
        String calculatedHirePenaltyPercentage = getHirePenaltyPercentage(hireStart, inv, claim.getClaimType().getPenaltyType());
        for (PenaltyCharge hirePenaltyPercentageValue : getHirePenaltyPercentages(hireStart, claim.getClaimType().getPenaltyType())) {
            if (hirePenaltyPercentageValue.getPenaltyPercentageDsc().equals(calculatedHirePenaltyPercentage)) {
                hirePenaltyAmout = (hirePenaltyPercentageValue.getPenaltyPercentage().divide(new BigDecimal(100)).multiply(hireGross)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return hirePenaltyAmout;
    }

    
    
    @Override
    public BigDecimal calculateRepairPenaltyCharge(Invoice inv, String repairPercentage, int penaltyType) {

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
        String calculatedRepairPenaltyPercentage = getRepairPenaltyPercentage(inv, claim.getClaimType().getPenaltyType());
        for (PenaltyCharge repairPenaltyPercentage : getRepairPenaltyPercentages(claim.getClaimType().getPenaltyType())) {
            if (repairPenaltyPercentage.getPenaltyPercentageDsc().equals(calculatedRepairPenaltyPercentage)) {
                repairPenaltyAmout = (repairPenaltyPercentage.getPenaltyPercentage().divide(new BigDecimal(100)).multiply(repairGross)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return repairPenaltyAmout;
    }
    
}
