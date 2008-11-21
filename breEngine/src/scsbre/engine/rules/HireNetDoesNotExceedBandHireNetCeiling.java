/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;
import scsbre.model.IClaimInfo;

/**
 *
 * @author Derm
 */
public class HireNetDoesNotExceedBandHireNetCeiling implements IBusinessRule {

    public boolean run(IClaimInfo claim) {
        return claim.getHireDetail().getNumberOfHireDays() <= claim.getChoBand().getHireDayCeiling();
    }

    public String getErrorMessage() {
        return "Number of hire days billed exceeds the CHO's hire days ceiling.";
    }

    public boolean isVisibleToCHO() {
        return false;
    }
    
    

}
