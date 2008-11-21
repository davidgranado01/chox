/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import scsbre.engine.util.*;
import scsbre.model.*;


/**
 *
 * @author Derm
 */
public class HasCalculatedCorrectDailyRate implements IBusinessRule {

    public boolean run(IClaimInfo claim) {
        
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        IVehicleClassInfo customerVClass = claim.getVClass();
        return cCalc.getDailyHireRateChargedWithToleranceDeduction().compareTo(customerVClass.getPrice()) <= 0;
    }

    public String getErrorMessage() {
        return "Daily rate billed for replacement vehicle class exceeds ABI rate.";
    }

    public boolean isVisibleToCHO() {
        return false;
    }
    

}
