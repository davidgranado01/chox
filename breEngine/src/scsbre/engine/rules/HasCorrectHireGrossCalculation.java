/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import scsbre.model.IClaimInfo;
import scsbre.engine.util.CalcHelper;
import scsbre.engine.util.InvoiceCalcHelper;
/**
 *
 * @author Derm
 * 
 * rule # 5
 */
public class HasCorrectHireGrossCalculation implements IBusinessRule {

    public boolean run(IClaimInfo claim) {
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice(), CalcHelper.VAT_RATE);
        return CalcHelper.EqualTo(claim.getInvoice().getHireGross(), iCalc.getCalculatedHireGross());
    }

    public String getErrorMessage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isVisibleToCHO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
