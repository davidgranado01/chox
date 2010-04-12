/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.xml.validations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import java.util.ArrayList;
import java.util.List;

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
//        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

            if (claimResult.getClaim() != null) {

                // CHECK DUPLICATE
                if (!claimResult.getClaim().getChoReference().equalsIgnoreCase("") && claimResult.getClaim().getChoReference() != null) {

                    if (choReferences.contains(claimResult.getClaim().getChoReference().toLowerCase().trim())) {
                        LOG.info("Duplicate Supplier Reference found: {}", claimResult.getClaim().getChoReference());
                        claimResult.setValid(false);
                        claimResult.getMessage().add("Duplicate Supplier Reference -  Supplier Reference already exists in bordereau");
                    } else {
                        choReferences.add(claimResult.getClaim().getChoReference().toLowerCase().trim());
                    }
                }


            }
//        }
    }
}
