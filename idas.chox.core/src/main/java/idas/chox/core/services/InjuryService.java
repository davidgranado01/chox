package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Injury;

public interface InjuryService {

    void saveInjuryForXMLUploader(final ClaimResult claimResult);

    Injury getInjury(int injuryId);

    void saveInjury(Injury injury);
}
