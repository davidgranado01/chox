/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.xmlValidation;

/**
 *
 * @author seeni
 */
public enum NonTpiHireMonitoringAndNewInvoiceRentalStatus {

    OFFHIRED {

        @Override
        public String description() {
            return "offhired";
        }
    },
    OFF_HIRED {

        @Override
        public String description() {
            return "off hired";
        }
    };

    public abstract String description();
}
