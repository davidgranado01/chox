package idas.chox.service.xml.readers;

import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.BordereauReaderContext;
import idas.chox.service.xml.validations.DataValidationParameter;

/**
 *
 * @author emmanuel
 */
public abstract class BaseEntityReader implements Reader {
    private static final Logger LOG = LoggerFactory.getLogger(BaseEntityReader.class);

    private BordereauReaderContext bordereauReaderContext;
    private DataValidationParameter dataValidationParameter;

    @Override
    public void execute(ClaimResult claimResult) throws DOMException, XPathExpressionException, Exception {

        LOG.debug("Validating claimResult");
        if (validate(claimResult)) {
            LOG.debug("Validated - processing claim");
            process(claimResult);
        } else {
            LOG.debug("Validation failed: {}", claimResult.getProcessStatus());
        }
    }

    protected abstract boolean validate(ClaimResult claimResult) throws Exception;

    protected abstract void process(ClaimResult claimResult) throws Exception;

    protected DataValidationParameter getDataValidationParameter() {
        if (dataValidationParameter == null) {
            dataValidationParameter = getBordereauReaderContext().getDataValidationParameter();
        }
        return dataValidationParameter;
    }

    @Override
    public void setBordereauReaderContext(BordereauReaderContext bordereauReaderContext) {
        this.bordereauReaderContext = bordereauReaderContext;
    }

    @Override
    public BordereauReaderContext getBordereauReaderContext() {
        return bordereauReaderContext;
    }
}
