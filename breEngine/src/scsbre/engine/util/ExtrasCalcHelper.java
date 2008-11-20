/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.util;

import java.math.BigDecimal;
import scsbre.model.IExtrasInfo;

/**
 *
 * @author Derm
 */
public class ExtrasCalcHelper {
    
    private IExtrasInfo extras;
    
    private ExtrasCalcHelper(IExtrasInfo ex){

        extras = ex;
    }
    
    public static ExtrasCalcHelper Create(IExtrasInfo ex){
        
        return new ExtrasCalcHelper(ex);
    }
    
    public BigDecimal getTotalExtras() {

        BigDecimal total = BigDecimal.ZERO;
        
        total = total.add(extras.getCdwFee());
        total = total.add(extras.getAutomaticFee());
        total = total.add(extras.getSatNavFee());
        total = total.add(extras.getEstateFee());
        total = total.add(extras.getBabySeatFee());
        total = total.add(extras.getTowBarsFee());
        total = total.add(extras.getNonStandardInsurancePremiumFee());
        total = total.add(extras.getAdminFee());
        total = total.add(extras.getRoofRackFee());
        total = total.add(extras.getDualControlFee());
        total = total.add(extras.getDeliveryCollectionFee());
        
        return total;
    }

}
