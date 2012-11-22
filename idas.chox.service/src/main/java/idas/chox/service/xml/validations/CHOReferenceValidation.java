package idas.chox.service.xml.validations;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.xmlValidation.ClaimResult;

/**
 *
 * @author emmanuel
 */
public class CHOReferenceValidation {

    private static final Logger LOG = LoggerFactory.getLogger(CHOReferenceValidation.class);
    List<String> choReferences;

    public CHOReferenceValidation() {
        choReferences = new ArrayList<String>();
    }

    public void validate(ClaimResult claimResult) {
        LOG.debug("Validating CHO references are unique");
            if (claimResult.getClaim() != null) {
                // CHECK DUPLICATE
                if (claimResult.getClaim().getChoReference() != null && !claimResult.getClaim().getChoReference().equalsIgnoreCase("") ) {
                    if (choReferences.contains(claimResult.getClaim().getChoReference().toLowerCase().trim())) {
                        LOG.debug("Duplicate Supplier Reference found: {}", claimResult.getClaim().getChoReference());
                        claimResult.setValid(false);
                        claimResult.getMessage().add("Duplicate Supplier Reference -  Supplier Reference already exists in bordereau");
                    } else {
                        choReferences.add(claimResult.getClaim().getChoReference().toLowerCase().trim());
                    }
                }
            }
    }
}
