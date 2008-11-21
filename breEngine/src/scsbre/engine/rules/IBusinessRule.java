package scsbre.engine.rules;

import scsbre.model.IClaimInfo;

public interface IBusinessRule {
    boolean run(IClaimInfo claim); 
    String getErrorMessage();
    boolean isVisibleToCHO();
}
