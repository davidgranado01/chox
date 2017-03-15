package idas.chox.service.xml.readers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.xmlValidation.ClaimResult;


/**
 *
 * @author John
 */
public class BordereauReader {
    private static final Logger LOG = LoggerFactory.getLogger(BordereauReader.class);
    private List<Reader> subEntityReaders;
    private BusinessRulesEngService businessRulesEngService;


    //Make sure thr xml document processing here contain valid format and schema
    public void execute(ClaimResult claimResult) throws Exception {

        try {
                if (LOG.isDebugEnabled() && claimResult.getClaim() != null) {
                    LOG.debug("Claim '{}' isCheckDataValid={}", claimResult.getClaim().getChoReference(), claimResult.isCheckDataValid());
                    LOG.debug("Claim '{}' isDataValid={}", claimResult.getClaim().getChoReference(), claimResult.isDataValid());
                    LOG.debug("Claim '{}' isValid={}", claimResult.getClaim().getChoReference(), claimResult.isValid());
                }
                else if (LOG.isDebugEnabled()) {
                    LOG.debug("No claim in claim result.");
                }
                for (Reader r : subEntityReaders) {
                    LOG.debug("Processing using reader {}", r.getClass().getSimpleName());
                    try {
                            r.execute(claimResult);                            
                    }
                    catch (Exception ex) {
                        claimResult.setValid(false);
                        claimResult.getMessage().add("Internal error has occurred - please report to CHOX support.");
                        LOG.error("Exception thrown reading claim with reader '{}':\n", r.getClass().getSimpleName(), ex);
                        if (ex.getCause() != null) {
                            LOG.error("    Caused by: {}", ex.getCause().getMessage());
                        }
                    }

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("    isCheckDataValid={}", claimResult.isCheckDataValid());
                        LOG.debug("    isDataValid={}", claimResult.isDataValid());
                        LOG.debug("    isValid={}", claimResult.isValid());
                    }

                    // If there is an error in the Claim Header reader then stop processing
                    // The ClaimHeader reader will always be the first reader called
                    if (!claimResult.isValid() && r.getClass() == ClaimHeaderReader.class) {
                        LOG.debug("Claim Header is invalid - stopping processing.");
                        break;
                    }
                }
        }
        catch (Exception ex) {
            LOG.warn("Exception caught processing xml file: {}", ex.getMessage());
        }
    }


    public void setSubEntityReaders(List<Reader> subEntityReaders) {
        this.subEntityReaders = subEntityReaders;
    }

    public void setBusinessRulesEngService(BusinessRulesEngService businessRulesEngService) {
        this.businessRulesEngService = businessRulesEngService;
    }
}