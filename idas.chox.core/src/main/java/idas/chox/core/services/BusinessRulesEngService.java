package idas.chox.core.services;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.xmlValidation.*;
import idas.chox.core.model.Claim;

public interface BusinessRulesEngService {

    ClaimResult execute(ClaimResult claimResult);

    RulesEngineResponse processResubmitInvoice(Claim claim);
}
