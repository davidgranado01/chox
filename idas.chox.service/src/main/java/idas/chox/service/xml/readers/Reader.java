/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.xml.readers;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.BordereauRederContext;

/**
 *
 * @author Carlson
 */
public interface Reader {

    public void execute(ClaimResult claimResult ) throws Exception;
    public void setBordereauRederContext(BordereauRederContext bordereauRederContext);
    public BordereauRederContext getBordereauRederContext();

}
