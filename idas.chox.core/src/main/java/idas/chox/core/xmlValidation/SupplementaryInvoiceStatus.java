/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.xmlValidation;

/**
 *
 * @author seeni
 */
public enum  SupplementaryInvoiceStatus {
   SUPPLEMENTARY_INVOICE {

        @Override
        public String description() {
            return "Supplementary Invoice";
        }
    };

    public abstract String description(); 
}
