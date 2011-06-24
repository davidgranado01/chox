/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.xmlValidation;

/**
 *
 * @author seeni
 */
public enum NonTpiRentalStatus {

    INPROGRESS {

        @Override
        public String description() {
            return "inprogress";
        }
    },
    IN_PROGRESS {

        @Override
        public String description() {
            return "in progress";
        }
    },
    COMPLETE {

        @Override
        public String description() {
            return "complete";
        }
    };

    public abstract String description();
}
