package idas.chox.data.services;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Invoice;
import idas.chox.core.model.InvoiceOriginal;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.xmlValidation.ClaimResult;

public class InvoiceServiceImpl extends SecureDataService implements InvoiceService {
    
    private ClaimService claimService;
    
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceServiceImpl.class);
    
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
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
            invOriginal.setCollaborationQtyOriginal(inv.getCollaborationQty());
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
            invOriginal.setCollaborationFeeOriginal(inv.getCollaborationFee());
            invOriginal.setMiscellaneousFeeOriginal(inv.getMiscellaneousFee());
            invOriginal.setAutomaticFeeOriginal(inv.getAutomaticFee());
            invOriginal.setSatNavFeeOriginal(inv.getSatNavFee());
            invOriginal.setEstateFeeOriginal(inv.getEstateFee());
            invOriginal.setBabySeatFeeOriginal(inv.getBabySeatFee());
            invOriginal.setTowBarsFeeOriginal(inv.getTowBarsFee());
            invOriginal.setNonStandardInsurancePremiumFeeOriginal(inv.getNonStandardInsurancePremiumFee());
            invOriginal.setAdminFeeOriginal(inv.getAdminFee());
            invOriginal.setRepairAdminFeeOriginal(inv.getRepairAdminFee());
            invOriginal.setRepairAcquisitionFeeOriginal(inv.getRepairAcquisitionFee());
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
            invOriginal.setPaymentTeam(inv.isPaymentTeam());
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
}
