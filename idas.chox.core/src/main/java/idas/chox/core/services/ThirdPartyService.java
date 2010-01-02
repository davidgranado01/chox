package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.ThirdParty;

public interface ThirdPartyService {

    public void saveThirdPartyForXMLUploader(final ClaimResult claimResult);

    public ThirdParty getThirdParty(int thirdPartyId);

    public void saveThirdParty(ThirdParty thirdParty);
}
