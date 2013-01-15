package idas.chox.service.xml.readers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.xmlValidation.ClaimResult;

/**
 *
 * @author emmanuel
 */
public class BordereauReader {
    private static final Logger LOG = LoggerFactory.getLogger(BordereauReader.class);

    private List<Reader> subEntityReaders;
    private BusinessRulesEngService businessRulesEngService;


    //Make sure thr xml document processing here contain valid format and schema
    public void execute(ClaimResult claimResult) throws Exception {

        try {
                if (claimResult.getClaim() != null) {
                    LOG.debug("Claim '{}' isCheckDataValid={}", claimResult.getClaim().getChoReference(), claimResult.isCheckDataValid());
                    LOG.debug("Claim '{}' isDataValid={}", claimResult.getClaim().getChoReference(), claimResult.isDataValid());
                    LOG.debug("Claim '{}' isValid={}", claimResult.getClaim().getChoReference(), claimResult.isValid());
                }
                else {
                LOG.debug("No claim in claim result.");
            }
                boolean firstReader = true;
                for (Reader r : subEntityReaders) {
                    LOG.debug("Processing using reader {}", r.getClass());
                    try {
                            r.execute(claimResult);                            
                    }
                    catch (Exception ex) {
                        LOG.error("Exception thrown reading claim with reader {}", r.getClass(), ex);
                        claimResult.setValid(false);
                        claimResult.getMessage().add("Internal error has occured - please report to CHOX support.");
                        if (ex.getCause() != null) {
                            LOG.error("    Caused by: {}", ex.getCause().getMessage());
                        }
                    }
                    
                    LOG.debug("    isCheckDataValid={}", claimResult.isCheckDataValid());
                    LOG.debug("    isDataValid={}", claimResult.isDataValid());
                    LOG.debug("    isValid={}", claimResult.isValid());

                    // If there is an error in the Claim Header reader then stop processing
                    // The ClaimHeader reader will always be the first reader called
                    if (firstReader && !claimResult.isValid()) {
                        LOG.debug("Claim Header is invalid - stopping processing.");
                        break;
                    }
                    firstReader = false;
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