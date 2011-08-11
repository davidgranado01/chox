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
    
    hire_monitor {

        @Override
        public String description() {
            return "hire monitor";
        }
    },
    hireMonitor {

        @Override
        public String description() {
            return "hireMonitor";
        }
    };

    public abstract String description();
}
