package idas.chox.service.xml.readers;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.BordereauRederContext;

public interface Reader {

    public void execute(ClaimResult claimResult ) throws Exception;
    public void setBordereauRederContext(BordereauRederContext bordereauRederContext);
    public BordereauRederContext getBordereauRederContext();

}
