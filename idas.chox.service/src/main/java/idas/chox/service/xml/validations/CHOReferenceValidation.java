/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.xml.validations;

import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emmanuel
 */
public class CHOReferenceValidation {

    List<String> choReferences;

    public CHOReferenceValidation() {
        choReferences = new ArrayList<String>();
    }

    public void validate(ClaimResult claimResult) {
        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

            if (claimResult.getClaim() != null) {

                // CHECK DUPLICATE
                if (!claimResult.getClaim().getChoReference().equalsIgnoreCase("") && claimResult.getClaim().getChoReference() != null) {

                    if (choReferences.contains(claimResult.getClaim().getChoReference().toLowerCase().trim())) {
                        claimResult.setValid(false);
                        claimResult.getMessage().add("Supplier Reference is already exist");
                    } else {
                        choReferences.add(claimResult.getClaim().getChoReference().toLowerCase().trim());
                    }
                }


            }
        }
    }
}
