package idas.chox.service.bre.util;

import java.math.BigDecimal;

import idas.chox.core.model.Invoice;

public final class ExtrasCalcHelper {
    
    private Invoice invoice;
    
    private ExtrasCalcHelper(Invoice ex){
        invoice = ex;
    }


    public static ExtrasCalcHelper getInstance(Invoice ex){
        return new ExtrasCalcHelper(ex);
    }


    public BigDecimal getTotalExtras() {
        BigDecimal total = BigDecimal.ZERO;
        
        total = total.add(invoice.getMiscellaneousFee());
        total = total.add(invoice.getCollaborationFee());
        total = total.add(invoice.getAutomaticFee());
        total = total.add(invoice.getAdditionalDriverFee());
        total = total.add(invoice.getSatNavFee());
        total = total.add(invoice.getEstateFee());
        total = total.add(invoice.getBabySeatFee());
        total = total.add(invoice.getTowBarsFee());
        total = total.add(invoice.getNonStandardInsurancePremiumFee());
        total = total.add(invoice.getAdminFee());
        total = total.add(invoice.getRoofRackFee());
        total = total.add(invoice.getDualControlFee());
        total = total.add(invoice.getDeliveryCollectionFee());
        total = total.add(invoice.getVedFee());
        
        return total;
    }
}

