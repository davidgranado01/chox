package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.ThirdParty;

public interface ThirdPartyService {

    void saveThirdPartyForXMLUploader(final ClaimResult claimResult);

    ThirdParty getThirdParty(int thirdPartyId);

    void saveThirdParty(ThirdParty thirdParty);
}
