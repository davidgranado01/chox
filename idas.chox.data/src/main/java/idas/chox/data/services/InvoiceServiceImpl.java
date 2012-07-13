package idas.chox.data.services;


import idas.chox.core.model.*;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerDiscountService;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.xmlValidation.ClaimResult;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class InvoiceServiceImpl extends SecureDataService implements InvoiceService {
    
    private ClaimService claimService;
    private InsurerDiscountService insurerDiscountService;
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceServiceImpl.class);
    
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setInsurerDiscountService(InsurerDiscountService insurerDiscountService) {
        this.insurerDiscountService = insurerDiscountService;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveInvoiceForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getClaim().getInvoice()) != null) {
            
            claimService.updateLiabilityPayment(claimResult.getClaim());
            
            
            getHibernateTemplate().saveOrUpdate((claimResult.getClaim().getInvoice()));
        }
    }

    @Override
    public Invoice getInvoice(int id) {
        return (Invoice) get(Invoice.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveInvoice(Invoice invoice) {

        save(invoice);
    }
    
    @Transactional(readOnly = false)
    @Override
    public InvoiceOriginal saveOriginalInvoice(Invoice inv) {
        try {

            InvoiceOriginal invOriginal = new InvoiceOriginal();
            invOriginal.setDateInvoicedOriginal(inv.getDateInvoiced());
            invOriginal.setMiscellaneousQtyOriginal(inv.getMiscellaneousQty());
            invOriginal.setAutomaticQtyOriginal(inv.getAutomaticQty());
            invOriginal.setSatNavQtyOriginal(inv.getSatNavQty());
            invOriginal.setEstateQtyOriginal(inv.getEstateQty());
            invOriginal.setBabySeatQtyOriginal(inv.getBabySeatQty());
            invOriginal.setTowBarsQtyOriginal(inv.getTowBarsQty());
            invOriginal.setNonStandardInsurancePremiumQtyOriginal(inv.getNonStandardInsurancePremiumQty());
            invOriginal.setAdminQtyOriginal(inv.getAdminQty());
            invOriginal.setRoofRackQtyOriginal(inv.getRoofRackQty());
            invOriginal.setDualControlQtyOriginal(inv.getDualControlQty());
            invOriginal.setDeliveryCollectionQtyOriginal(inv.getDeliveryCollectionQty());
            invOriginal.setHireNetOriginal(inv.getHireNet());
            invOriginal.setHireVatOriginal(inv.getHireVat());
            invOriginal.setHireGrossOriginal(inv.getHireGross());
            invOriginal.setRepairNetOriginal(inv.getRepairNet());
            invOriginal.setRepairVatOriginal(inv.getRepairVat());
            invOriginal.setRepairGrossOriginal(inv.getRepairGross());
            invOriginal.setEngineerFeeNetOriginal(inv.getEngineerFeeNet());
            invOriginal.setEngineerFeeVatOriginal(inv.getEngineerFeeVat());
            invOriginal.setEngineerFeeGrossOriginal(inv.getEngineerFeeGross());
            invOriginal.setStorageRecoveryNetOriginal(inv.getStorageRecoveryNet());
            invOriginal.setStorageRecoveryVatOriginal(inv.getStorageRecoveryVat());
            invOriginal.setStorageRecoveryGrossOriginal(inv.getStorageRecoveryGross());
            invOriginal.setTotalNetOriginal(inv.getTotalNet());
            invOriginal.setTotalVatOriginal(inv.getTotalVat());
            invOriginal.setTotalGrossOriginal(inv.getTotalGross());
            invOriginal.setClaimsHandlingInvoiceAmountOriginal(inv.getClaimsHandlingInvoiceAmount());
            invOriginal.setDeductionForClaimsHandlingFeeOriginal(inv.getDeductionForClaimsHandlingFee());
            invOriginal.setDiscountOriginal(inv.getDiscount());
            invOriginal.setFullTotalToPayOriginal(inv.getFullTotalToPay());
            invOriginal.setMiscellaneousFeeOriginal(inv.getMiscellaneousFee());
            invOriginal.setAutomaticFeeOriginal(inv.getAutomaticFee());
            invOriginal.setSatNavFeeOriginal(inv.getSatNavFee());
            invOriginal.setEstateFeeOriginal(inv.getEstateFee());
            invOriginal.setBabySeatFeeOriginal(inv.getBabySeatFee());
            invOriginal.setTowBarsFeeOriginal(inv.getTowBarsFee());
            invOriginal.setNonStandardInsurancePremiumFeeOriginal(inv.getNonStandardInsurancePremiumFee());
            invOriginal.setAdminFeeOriginal(inv.getAdminFee());
            invOriginal.setRoofRackFeeOriginal(inv.getRoofRackFee());
            invOriginal.setDualControlFeeOriginal(inv.getDualControlFee());
            invOriginal.setDeliveryCollectionFeeOriginal(inv.getDeliveryCollectionFee());
            invOriginal.setHireRateChargedPerDayOriginal(inv.getHireRateChargedPerDay());
            invOriginal.setExcessAmountCollectedOriginal(inv.getExcessAmountCollected());
            invOriginal.setVatAmountCollectedOriginal(inv.getVatAmountCollected());
            invOriginal.setVersion(0);
            invOriginal.setTotalToPayOriginal(inv.getTotalToPay());
            invOriginal.setAdditionalDriverFeeOriginal(inv.getAdditionalDriverFee());
            invOriginal.setAdditionalDriverQtyOriginal(inv.getAdditionalDriverQty());
            invOriginal.setTotalLossFeeNetOriginal(inv.getTotalLossFeeNet());
            invOriginal.setTotalLossFeeVatOriginal(inv.getTotalLossFeeVat());
            invOriginal.setTotalLossFeeGrossOriginal(inv.getTotalLossFeeGross());
            invOriginal.setInsurerDiscountOriginal(inv.getInsurerDiscount());
            invOriginal.setRepairGrossInsurerDiscountOriginal(inv.getRepairGrossInsurerDiscount());
            invOriginal.setHireGrossInsurerDiscountOriginal(inv.getHireGrossInsurerDiscount());
            save(invOriginal);
            return invOriginal;
            
        } catch (Exception ex) {
            LOG.error("Exception thrown when saving original invoice. Exception is ", ex);
            return null;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteOriginalInvoice(Invoice invoice) {
        try {
            delete(invoice.getInvoiceOriginal());
        } catch (Exception ex) {
            LOG.error("Exception thrown when deleting original invoice. Exception is ", ex);
        }
    }
    
    @Override
    public int getNoOfRejectedInvoices(Integer reasonOfRejectionId) {
        Criteria criteria = getSession().createCriteria(Invoice.class);
        criteria.add(Restrictions.eq("reasonOfRejection.id", reasonOfRejectionId));
        return countInvoices(criteria).intValue();
    }
    
    private Integer countInvoices(Criteria criteria) {
        criteria.setProjection(Projections.rowCount());
        List totalCountResult = criteria.list();
        criteria.setProjection(null);
        return ((Long) totalCountResult.get(0)).intValue();
    }
    
    @Override
    public int calculatePenaltyAlertQty(Invoice inv) {
        long dateDiff = inv.getInvoicedDays();
        return (int) (dateDiff / 30);
    }
    
    @Override
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean updateAutomaticPenaltyCharge(Claim claim) {
        LOG.debug("Updating penalty charges: claim.isAutoPenaltyChargeEnabled()={}, claim.getChorganisation().isAutoPenaltyChargeEnabled()={}, "
                + "!ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus())={}, claim.getInvoice()={}, "
                + "calculatePenaltyAlertQty(claim.getInvoice())={}, claim.getInvoice().getPenaltyAlertQty()={}, "
                + "calculatePenaltyAlertQty(claim.getInvoice())={}",
                new Object[]{claim.isAutoPenaltyChargeEnabled(), claim.getChorganisation().isAutoPenaltyChargeEnabled(),
                    !ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus()),
                    claim.getInvoice(), calculatePenaltyAlertQty(claim.getInvoice()),
                    claim.getInvoice().getPenaltyAlertQty(),
                    calculatePenaltyAlertQty(claim.getInvoice())});

        if (claim.isAutoPenaltyChargeEnabled()
                && claim.getChorganisation().isAutoPenaltyChargeEnabled()
                && !ClaimStatus.isInPenaltyChargeExclusionStatus(claim.getStatus())
                && claim.getInvoice() != null
                && claim.getInvoice().getInvoicedDays() > 30) {
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
                return true;
            } catch (Exception ex) {
                LOG.error("Exception thrown while updating auto penalty charge store procedure for claim '{}'", claim.getChoReference(), ex);
                return false;
            }
        }
        return false;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updatePenaltyStartDate(Claim claim, Date autoPenaltyStart) {

        Invoice inv = claim.getInvoice();
        inv.setFullTotalToPay(inv.getFullTotalToPay().subtract(inv.getHirePenaltyCharge()).subtract(inv.getRepairPenaltyCharge()));
        inv.setAutoPenaltyStart(autoPenaltyStart);
        inv.setHirePenaltyPercentage(null);
        inv.setRepairPenaltyPercentage(null);
        inv.setHirePenaltyCharge(BigDecimal.ZERO);
        inv.setRepairPenaltyCharge(BigDecimal.ZERO);
        inv.setPenaltyAlertQty(0);
        inv.setAutoPenaltyAlertQty(0);
        inv.setHirePenaltyChargeAppliedDate(null);
        inv.setRepairPenaltyChargeAppliedDate(null);
        if (inv.getTotalPenaltyCharge() != null && inv.getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) > 0) {
            Comment comment = Comment.New(0, "Penalty charges have been removed from the invoice as the date from which penalty charges are calculated has been manually updated.");
            claim.addComment(comment);
        }
        inv.setTotalPenaltyCharge(BigDecimal.ZERO);

        claimService.updateClaim(claim);
    }
        
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean setPenaltyStartToDateInvoiced(String choReference) {
        Claim claim = claimService.getClaimByCHOReferenceNumber(choReference);
        if (claim != null && claim.getInvoice() != null) {
            updatePenaltyStartDate(claim, claim.getInvoice().getDateInvoiced());
            updateAutomaticPenaltyCharge(claim);
            applyInsurerDiscounts(claim, null, false);
            return true;
        }

        return false;
    }
    
    @Override
    public BigDecimal calculateHirePenaltyCharge(Invoice inv, String hirePercentage, Date hireStart) {

        BigDecimal hirePenaltyAmout = BigDecimal.ZERO.setScale(2);
//        BigDecimal hireNet = claim.getInvoice().getHireNet();
        BigDecimal hireGross = inv.getHireGross();
//        Date hireStart = claim.getVehicleHire() != null ? claim.getVehicleHire().getHireStart() : claim.getInvoice().getDateInvoiced();

        for (PenaltyPercentage hirePenaltyPercentageValue : PenaltyPercentage.getHirePenaltyPercentages(hireStart)) {
            if (hirePenaltyPercentageValue.getPercentage().equals(hirePercentage)) {
//                BigDecimal hirePenaltyWithoutVat = hirePenaltyPercentageValue.getPercentageValue().divide(new BigDecimal(100)).multiply(hireNet);
//                hirePenaltyAmout = hirePenaltyWithoutVat.add(hirePenaltyWithoutVat.multiply(CalcHelper.VAT_RATE)).setScale(2, RoundingMode.HALF_UP);
                hirePenaltyAmout = (hirePenaltyPercentageValue.getPercentageValue().divide(new BigDecimal(100)).multiply(hireGross)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return hirePenaltyAmout;
    }

    @Override
    public BigDecimal calculateHirePenaltyCharge(Claim claim) {

        Invoice inv = claim.getInvoice();
        BigDecimal hirePenaltyAmout = BigDecimal.ZERO.setScale(2);
        BigDecimal hireGross = inv.getHireGross();
        Date hireStart = claim.getVehicleHire() != null ? claim.getVehicleHire().getHireStart() : claim.getInvoice().getDateInvoiced();
        String calculatedHirePenaltyPercentage = calculatedHirePenaltyPercentage(inv, hireStart);
        for (PenaltyPercentage hirePenaltyPercentageValue : PenaltyPercentage.getHirePenaltyPercentages(hireStart)) {
            if (hirePenaltyPercentageValue.getPercentage().equals(calculatedHirePenaltyPercentage)) {
                hirePenaltyAmout = (hirePenaltyPercentageValue.getPercentageValue().divide(new BigDecimal(100)).multiply(hireGross)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return hirePenaltyAmout;
    }

    
    
    @Override
    public BigDecimal calculateRepairPenaltyCharge(Invoice inv, String repairPercentage) {

        BigDecimal repairPenaltyAmout = BigDecimal.ZERO.setScale(2);
//        BigDecimal repairNet = claim.getInvoice().getRepairNet();
        BigDecimal repairGross = inv.getRepairGross();

        for (PenaltyPercentage repairPenaltyPercentage : PenaltyPercentage.getRepairPenaltyPercentages()) {
            if (repairPenaltyPercentage.getPercentage().equals(repairPercentage)) {
//                BigDecimal repairPenaltyWithoutVat = repairPenaltyPercentage.getPercentageValue().divide(new BigDecimal(100)).multiply(repairNet);
//                repairPenaltyAmout = repairPenaltyWithoutVat.add(repairPenaltyWithoutVat.multiply(CalcHelper.VAT_RATE)).setScale(2, RoundingMode.HALF_UP);
                repairPenaltyAmout = (repairPenaltyPercentage.getPercentageValue().divide(new BigDecimal(100)).multiply(repairGross)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return repairPenaltyAmout;
    }
      
    @Override
    public BigDecimal calculateRepairPenaltyCharge(Claim claim) {

        Invoice inv = claim.getInvoice();
        BigDecimal repairPenaltyAmout = BigDecimal.ZERO.setScale(2);
        BigDecimal repairGross = inv.getRepairGross();
        String calculatedRepairPenaltyPercentage = calculatedRepairPenaltyPercentage(inv);
        for (PenaltyPercentage repairPenaltyPercentage : PenaltyPercentage.getRepairPenaltyPercentages()) {
            if (repairPenaltyPercentage.getPercentage().equals(calculatedRepairPenaltyPercentage)) {
                repairPenaltyAmout = (repairPenaltyPercentage.getPercentageValue().divide(new BigDecimal(100)).multiply(repairGross)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return repairPenaltyAmout;
    }
    
    @Override
    public String calculatedHirePenaltyPercentage(Invoice inv, Date hireStart) {

        if (inv.getHireNet().compareTo(BigDecimal.ZERO) == 1) {
            int penaltyAlertQty = calculatePenaltyAlertQty(inv);
            return PenaltyPercentage.getHirePenaltyPercentage(hireStart, penaltyAlertQty);
        } else {
            return PenaltyPercentage.ZERO_PERCENTAGE.getPercentage();
        }

    }

    @Override
    public String calculatedRepairPenaltyPercentage(Invoice inv) {

        if (inv.getRepairNet().compareTo(BigDecimal.ZERO) == 1) {
            int penaltyAlertQty = calculatePenaltyAlertQty(inv);
            return PenaltyPercentage.getRepairPenaltyPercentage(penaltyAlertQty);
        } else {
            return PenaltyPercentage.ZERO_PERCENTAGE.getPercentage();
        }

    }
    
    @Override
    public void applyInsurerDiscounts(Claim claim, WebUser user, boolean canAddComment) {

        if (claim.getInsurer().isInsurerDiscountEnable()) {
            Invoice inv = claim.getInvoice();
            BigDecimal insurerDiscountAmount = BigDecimal.ZERO;
            BigDecimal totalGrossInsurerDiscountAmount = BigDecimal.ZERO;
            BigDecimal repairGrossInsurerDiscountAmount = BigDecimal.ZERO;
            BigDecimal hireGrossInsurerDiscountAmount = BigDecimal.ZERO;

            List<InsurerDiscount> insurerDiscounts = insurerDiscountService.getInsurerDiscount(claim.getChorganisation().getId(), claim.getInsurer().getId());

            boolean isRepairGrossDiscountAppliedToPenalties = false;
            boolean isHireGrossDiscountAppliedToPenalties = false;
            boolean repairGrossInsurerDiscountEnabled = false;
            boolean hireGrossInsurerDiscountEnabled = false;
            boolean totalGrossInsurerDiscountEnabled = false;
            BigDecimal hireGrossInsurerDiscountPercentage = BigDecimal.ZERO;
            BigDecimal repairGrossInsurerDiscountPercentage = BigDecimal.ZERO;
            BigDecimal totalGrossInsurerDiscountPercentage = BigDecimal.ZERO;

            for (InsurerDiscount insurerDiscount : insurerDiscounts) {

                if (insurerDiscount.getInsurerDiscountType().equals(InsurerDiscountType.REPAIR)) {
                    isRepairGrossDiscountAppliedToPenalties = insurerDiscount.isAppliedToPenalties();
                }
                if (insurerDiscount.getInsurerDiscountType().equals(InsurerDiscountType.HIRE)) {
                    isHireGrossDiscountAppliedToPenalties = insurerDiscount.isAppliedToPenalties();
                }
            }

            for (InsurerDiscountType insurerDiscountType : InsurerDiscountType.values()) {

                if (insurerDiscountType.getInsurerDiscountTypeValue() == InsurerDiscountType.REPAIR.getInsurerDiscountTypeValue() && inv.getRepairGross().compareTo(BigDecimal.ZERO) == 1) {
                    repairGrossInsurerDiscountPercentage = insurerDiscountService.getDiscountPercentage(claim.getInsurer().getId(), claim.getChorganisation().getId(),
                            inv.getCreatedDate(), insurerDiscountType.getInsurerDiscountTypeValue());
                    if (repairGrossInsurerDiscountPercentage.compareTo(BigDecimal.ZERO) == 1) {
                        repairGrossInsurerDiscountEnabled = true;
                    }
                }
                if (insurerDiscountType.getInsurerDiscountTypeValue() == InsurerDiscountType.HIRE.getInsurerDiscountTypeValue() && inv.getHireGross().compareTo(BigDecimal.ZERO) == 1) {
                    hireGrossInsurerDiscountPercentage = insurerDiscountService.getDiscountPercentage(claim.getInsurer().getId(), claim.getChorganisation().getId(),
                            inv.getCreatedDate(), insurerDiscountType.getInsurerDiscountTypeValue());
                    if (hireGrossInsurerDiscountPercentage.compareTo(BigDecimal.ZERO) == 1) {
                        hireGrossInsurerDiscountEnabled = true;
                    }
                }
                if (insurerDiscountType.getInsurerDiscountTypeValue() == InsurerDiscountType.TOTAL.getInsurerDiscountTypeValue() && inv.getTotalGross().compareTo(BigDecimal.ZERO) == 1) {
                    totalGrossInsurerDiscountPercentage = insurerDiscountService.getDiscountPercentage(claim.getInsurer().getId(),
                            claim.getChorganisation().getId(), inv.getCreatedDate(), insurerDiscountType.getInsurerDiscountTypeValue());
                    if (totalGrossInsurerDiscountPercentage.compareTo(BigDecimal.ZERO) == 1) {
                        totalGrossInsurerDiscountEnabled = true;
                    }
                }
            }

//            for (InsurerDiscountType insurerDiscountType : InsurerDiscountType.values()) {

//                if (insurerDiscountType.getInsurerDiscountTypeValue() == InsurerDiscountType.HIRE.getInsurerDiscountTypeValue() && inv.getHireGross().compareTo(BigDecimal.ZERO) == 1) {
//                    BigDecimal hireGrossInsurerDiscountPercentage = insurerDiscountService.getDiscountPercentage(claim.getInsurer().getId(), claim.getChorganisation().getId(),
//                            inv.getCreatedDate(), insurerDiscountType.getInsurerDiscountTypeValue());
            if (hireGrossInsurerDiscountEnabled) {
                hireGrossInsurerDiscountAmount = BigDecimal.ZERO;
                if (isHireGrossDiscountAppliedToPenalties) {
                    Date hireStart = claim.getVehicleHire() != null ? claim.getVehicleHire().getHireStart() : inv.getDateInvoiced();
                    BigDecimal hirePenaltyAmount = calculateHirePenaltyCharge(inv, inv.getHirePenaltyPercentage(), hireStart);
                    hireGrossInsurerDiscountAmount = (inv.getHireGross().add(hirePenaltyAmount)).multiply(hireGrossInsurerDiscountPercentage.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
                } else {
                    hireGrossInsurerDiscountAmount = inv.getHireGross().multiply(hireGrossInsurerDiscountPercentage.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
                }
                LOG.debug("hire gross insurer discount calculated {}", hireGrossInsurerDiscountAmount);
                LOG.debug("hire gross insurer discount original {}.", hireGrossInsurerDiscountAmount);
                
                if (canAddComment && user != null && hireGrossInsurerDiscountAmount.compareTo(inv.getHireGrossInsurerDiscount().multiply(BigDecimal.valueOf(-1))) != 0 
                        && hireGrossInsurerDiscountAmount.compareTo(BigDecimal.ZERO) == 1) {
                    addInsurerDiscountComment(claim, hireGrossInsurerDiscountAmount, hireGrossInsurerDiscountPercentage, InsurerDiscountType.HIRE.toString(), user);
                }
                
                inv.setHireGrossInsurerDiscount(hireGrossInsurerDiscountAmount.multiply(BigDecimal.valueOf(-1)));
                
            }
//                }

//                if (insurerDiscountType.getInsurerDiscountTypeValue() == InsurerDiscountType.REPAIR.getInsurerDiscountTypeValue() && inv.getRepairGross().compareTo(BigDecimal.ZERO) == 1) {
//                    BigDecimal repairGrossInsurerDiscountPercentage = insurerDiscountService.getDiscountPercentage(claim.getInsurer().getId(), claim.getChorganisation().getId(),
//                            inv.getCreatedDate(), insurerDiscountType.getInsurerDiscountTypeValue());
            if (repairGrossInsurerDiscountEnabled) {
                repairGrossInsurerDiscountAmount = BigDecimal.ZERO;
                if (isRepairGrossDiscountAppliedToPenalties) {
                    BigDecimal repairPenaltyAmount = calculateRepairPenaltyCharge(inv, inv.getRepairPenaltyPercentage());
                    repairGrossInsurerDiscountAmount = (inv.getRepairGross().add(repairPenaltyAmount)).multiply(repairGrossInsurerDiscountPercentage.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
                } else {
                    repairGrossInsurerDiscountAmount = inv.getRepairGross().multiply(repairGrossInsurerDiscountPercentage.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
                }
                LOG.debug("repair gross insurer discount calculated {}.", repairGrossInsurerDiscountAmount);
                LOG.debug("repair gross insurer discount original {}.", inv.getRepairGrossInsurerDiscount());
                if (canAddComment && user != null && repairGrossInsurerDiscountAmount.compareTo(inv.getRepairGrossInsurerDiscount().multiply(BigDecimal.valueOf(-1))) != 0 
                        && repairGrossInsurerDiscountAmount.compareTo(BigDecimal.ZERO) == 1) {
                    addInsurerDiscountComment(claim, repairGrossInsurerDiscountAmount, repairGrossInsurerDiscountPercentage, InsurerDiscountType.REPAIR.toString(), user);
                }
                
                inv.setRepairGrossInsurerDiscount(repairGrossInsurerDiscountAmount.multiply(BigDecimal.valueOf(-1)));
                
            }
//                }

//                if (insurerDiscountType.getInsurerDiscountTypeValue() == InsurerDiscountType.TOTAL.getInsurerDiscountTypeValue() && inv.getTotalGross().compareTo(BigDecimal.ZERO) == 1) {
//                    BigDecimal totalGrossInsurerDiscountPercentage = insurerDiscountService.getDiscountPercentage(claim.getInsurer().getId(),
//                            claim.getChorganisation().getId(), inv.getCreatedDate(), insurerDiscountType.getInsurerDiscountTypeValue());
            if (totalGrossInsurerDiscountEnabled) {

                BigDecimal grossValueCombined = BigDecimal.ZERO;
                BigDecimal totalGrossValue = BigDecimal.ZERO;

                if (hireGrossInsurerDiscountEnabled) {
                    grossValueCombined = inv.getHireGross();
                }
                if (repairGrossInsurerDiscountEnabled) {
                    grossValueCombined = grossValueCombined.add(inv.getRepairGross());
                }
                totalGrossValue = inv.getTotalGross().subtract(grossValueCombined);

                totalGrossInsurerDiscountAmount = totalGrossValue.multiply(totalGrossInsurerDiscountPercentage.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
                
                LOG.debug("total gross insurer discount calculated {}.", totalGrossInsurerDiscountAmount);
                LOG.debug("total gross insurer discount original {}.", inv.getTotalGrossInsurerDiscount());
                if (canAddComment && user != null && totalGrossInsurerDiscountAmount.compareTo(inv.getTotalGrossInsurerDiscount().multiply(BigDecimal.valueOf(-1))) != 0 
                        && totalGrossInsurerDiscountAmount.compareTo(BigDecimal.ZERO) == 1) {
                    addInsurerDiscountComment(claim, totalGrossInsurerDiscountAmount, totalGrossInsurerDiscountPercentage, InsurerDiscountType.TOTAL.toString().toString(), user);
                }
                
                inv.setTotalGrossInsurerDiscount(totalGrossInsurerDiscountAmount.multiply(BigDecimal.valueOf(-1)));
                
            }
//                }
//            }
            inv.setAverageInsurerDiscountPercentageApplied((totalGrossInsurerDiscountPercentage.add(repairGrossInsurerDiscountPercentage)
                    .add(hireGrossInsurerDiscountPercentage))
                    .divide(new BigDecimal(3), 4, BigDecimal.ROUND_HALF_UP));
            insurerDiscountAmount = totalGrossInsurerDiscountAmount.add(repairGrossInsurerDiscountAmount).add(hireGrossInsurerDiscountAmount);
            LOG.debug("total insurer discount calculated {}.", insurerDiscountAmount);
            LOG.debug("full total to pay before insurer discount is {}.", inv.getFullTotalToPay());
            LOG.debug("insurer discount original {}.", inv.getInsurerDiscount());
            inv.setFullTotalToPay(inv.getFullTotalToPay().subtract(inv.getInsurerDiscount()).subtract(insurerDiscountAmount));
            LOG.debug("total to pay after insurer discount applied {}.", inv.getFullTotalToPay());
            inv.setInsurerDiscount(insurerDiscountAmount.multiply(BigDecimal.valueOf(-1)));
        }
    }
    
    public void addInsurerDiscountComment(Claim claim, BigDecimal insurerDiscountAmount, BigDecimal insurerDiscountPercentage, String insurerDiscountType, WebUser user) {
        Comment comment = Comment.New(0, "A discount of £" + insurerDiscountAmount + " (" + insurerDiscountPercentage + "%) " + "has been applied to " + "the " + insurerDiscountType + " on this invoice based on the discount contract in place.");
        comment.setRaisedBy(user);
        claim.addComment(comment);
    }

}
