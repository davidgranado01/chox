package idas.chox.core.services;

import idas.chox.core.bre.RulesEngine;
import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Claim;

public interface BusinessRulesEngService {

//    void process(ClaimResult claimResult);

    RulesEngineResponse processResubmitInvoice(Claim claim);

    void setRulesEngine(RulesEngine rulesEngine);

    RulesEngine getRulesEngine();
}
