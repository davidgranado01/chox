package scsbre.engine.rules;

import scsbre.model.*;
import scsbre.engine.rules.IBusinessRule;

public class HasAllowedVehicleClass implements IBusinessRule {

    public boolean run(IClaimInfo claim) {

        return claim.getHireDetail().getVClass().getPrice().compareTo(claim.getVClass().getPrice()) <= 0;
    }

    public String getErrorMessage() {
        return "Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.";
    }

    public boolean isVisibleToCHO() {
        return false;
    }
}
