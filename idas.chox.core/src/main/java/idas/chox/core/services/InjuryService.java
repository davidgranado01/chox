package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Injury;

public interface InjuryService {

    public void saveInjuryForXMLUploader(final ClaimResult claimResult);

    public Injury getInjury(int injuryId);

    public void saveInjury(Injury injury);
}
