package idas.chox.service.xml.readers;

import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.xmlValidation.ClaimResult;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                else
                    LOG.debug("No claim in claim result.");
                for (Reader r : subEntityReaders) {
                    if (claimResult.getClaim() != null)
                        LOG.debug("Processing subEntityReaders for claim '{}'...", claimResult.getClaim().getChoReference());
                    else
                        LOG.debug("Processing subEntityReaders (no claim in claimResult).");
                    try {
                        if(claimResult.isValid()){
                            r.execute(claimResult); 
                        }else{
                           LOG.debug("claimResult is not valid."); 
                        }
                           
                    }
                    catch (Exception ex) {
                        LOG.error("Exception thrown reading claim with reader {}: {}", r.getClass(), ex.getMessage());
                        if (ex.getCause() != null) {
                            LOG.error("    Caused by: {}", ex.getCause().getMessage());
                        }
                    }
                    LOG.debug("    isCheckDataValid={}", claimResult.isCheckDataValid());
                    LOG.debug("    isDataValid={}", claimResult.isDataValid());
                    LOG.debug("    isValid={}", claimResult.isValid());
                    if (claimResult.getClaim() != null)
                        LOG.debug("Done Processing subEntityReaders for claim '{}'.", claimResult.getClaim().getChoReference());
                    else
                        LOG.debug("Done Processing subEntityReaders (no claim in claimResult).");
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