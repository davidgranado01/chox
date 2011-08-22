/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.xmlValidation;

/**
 *
 * @author seeni
 */
public enum NonTpiHireMonitoringRentalStatus {
    
    hire_monitoring {

        @Override
        public String description() {
            return "hire monitoring";
        }
    },
    hireMonitoring {

        @Override
        public String description() {
            return "hireMonitoring";
        }
    };

    public abstract String description();
}
