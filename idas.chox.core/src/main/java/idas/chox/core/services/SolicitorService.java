package idas.chox.core.services;

import idas.chox.core.model.Injury;
import idas.chox.core.model.Solicitor;
import idas.chox.core.xmlValidation.ClaimResult;

public interface SolicitorService {

    public void saveSolicitorForXMLUploader(final ClaimResult claimResult);

    public Solicitor getSolicitor(int solicitorId);

    public void saveSolicitor(Solicitor solicitor);

    public Solicitor getSolicitorByInjury(Injury injury);
}
