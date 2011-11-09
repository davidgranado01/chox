package idas.chox.service.xml.readers;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.BordereauReaderContext;

public interface Reader {

    public void execute(ClaimResult claimResult ) throws Exception;
    public void setBordereauReaderContext(BordereauReaderContext bordereauRedearContext);
    public BordereauReaderContext getBordereauReaderContext();

}
