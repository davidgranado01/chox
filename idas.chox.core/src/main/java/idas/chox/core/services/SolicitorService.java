package idas.chox.core.services;

import idas.chox.core.model.Injury;
import idas.chox.core.model.Solicitor;
import idas.chox.core.xmlValidation.ClaimResult;

public interface SolicitorService {

    void saveSolicitorForXMLUploader(final ClaimResult claimResult);

    Solicitor getSolicitor(int solicitorId);

    void saveSolicitor(Solicitor solicitor);

    Solicitor getSolicitorByInjury(Injury injury);
}
