package idas.chox.core.model;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VehicleClass extends Entity implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(VehicleClass.class);

    /**
     * This attribute maps to the column name in the vehicle_class table.
     */
    private String name;

    /**
     * Method 'VehicleClass'
     *
     */
    public VehicleClass() {
    }

    /**
     * Method 'getName'
     *
     * @return java.lang.String
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Method 'setName'
     *
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    static public boolean isPrestige(String className) {
        /* Vehicle classes considered 'Prestige & Special Vehicles' are:
         *      M5-M6, F4-F9, P1-P13, SP4-SP13, PV1-PV6, CV1-CV4,
         *      RV1-RV2, CP1-CP3, CS1-CS5, CM1-CM3, T5-T14.
         *      PT9 & PT13, B4-B6
         */
        if (className == null || className.length()==0) {
            return false;
        }
        
        if (className.startsWith("M5") || className.startsWith("M6")
                || className.startsWith("F4") || className.startsWith("F5")
                || className.startsWith("F6") || className.startsWith("F7")
                || className.startsWith("F8") || className.startsWith("F9")
                || isPClass(className)
                || className.startsWith("SP4") || className.startsWith("SP5")
                || className.startsWith("SP6") || className.startsWith("SP7")
                || className.startsWith("SP8") || className.startsWith("SP9")
                || className.startsWith("SP10") || className.startsWith("SP11")
                || className.startsWith("SP12") || className.startsWith("SP13")
                || className.startsWith("PV")
                || className.startsWith("CV")
                || className.startsWith("RV")
                || className.startsWith("CP")
                || className.startsWith("CS")
                || className.startsWith("CM")
                || className.startsWith("T5") || className.startsWith("T6")
                || className.startsWith("T7") || className.startsWith("T8")
                || className.startsWith("T9") || className.startsWith("T10")
                || className.startsWith("T11") || className.startsWith("T12")
                || className.startsWith("T13") || className.startsWith("T14")
                || className.startsWith("PT9") || className.startsWith("PT13")
                || className.startsWith("B4") || className.startsWith("B5") || className.startsWith("B6")) {
            return true;
        }
        
        return false;
    }
    
    static public boolean isCommercialPrivateOrTaxi(String className) {
        /*
         * All Ts
         * All PTs
         * All PVs
         * All CVs
         * All RVs
         * All CPs
         * All CSs
         * All CMs
         */
        if (className.charAt(0) == 'T' || className.startsWith("PT") || className.startsWith("NT")
               || className.startsWith("PV")
               || className.startsWith("CV") || className.startsWith("RV")
               || className.startsWith("CP") || className.startsWith("CS")
               || className.startsWith("CM") ) {
            return true;
        }

        return false;
    }
    static public boolean isPClass(String className) {
        if (className.charAt(0) == 'P' && className.charAt(1) >= '1' && className.charAt(1) <= '9') {
            return true;
        }

        return false;
    }
    static public boolean isPOrSClass(String className) {
        if (className.charAt(0) == 'S' && className.charAt(1) >= '1' && className.charAt(1) <= '9') {
            return true;
        }

        return isPClass(className);
    }
    static public boolean isTOrPTClass(String className) {
        if (className.charAt(0) == 'T' && className.charAt(1) >= '1' && className.charAt(1) <= '9') {
            return true;
        }
        else if(className.charAt(0) == 'P' && className.charAt(1) == 'T'  && className.charAt(2) >= '1'
                && className.charAt(2) <= '9') {
            return true;
        }
        else if(className.charAt(0) == 'N' && className.charAt(1) == 'T'  && className.charAt(2) >= '3'
                && className.charAt(2) <= '4') {
            return true;
        }

        return false;
    }

    static public boolean isPTClass(String className) {
        if(className.charAt(0) == 'P' && className.charAt(1) == 'T'  && className.charAt(2) >= '1'
                && className.charAt(2) <= '9') {
            return true;
        }

        return false;
    }
    

    static public int classPDifference(VehicleClass class1, VehicleClass class2) {
        // if class1 <  class2 then return a positive number
        // indicating the difference between the two class types,
        // e.g. classDifference(P3, P5) = 2
        //      classDifference(P5, P3) = -2
        //      classDifference(P1, P1) = 0

        int class1No = getPClassNumber(class1.getName());
        LOG.debug("Class no for '{}' is {}", class1.getName(), class1No);
        
        if (class1No < 0) {
            throw new IllegalArgumentException("Cannot compare non-prestige vehicle.");
        }

        int class2No = getPClassNumber(class2.getName());
        LOG.debug("Class no for '{}' is {}", class2.getName(), class2No);
        if (class2No > 0) {
            // Both Ps
            return class2No - class1No;
        }
        
        class2No = getSClassNumber(class2.getName());
        // If class 2 not S class, we can't compare
        if (class2No < 0) {
            throw new IllegalArgumentException("Cannot compare prestige vehicle to class " + class2.getName() + ".");
        }

        // Different classes - we are only considering P and S classes
        return -class1No - (7-class2No); // assunimg best S-class is S7
    }

    
    static public int classSPDifference(VehicleClass class1, VehicleClass class2) {
        // if class1 <  class2 then return a positive number
        // indicating the difference between the two class types,
        // e.g. classDifference(SP3, SP5) = 2
        //      classDifference(SP5, SP3) = -2
        //      classDifference(SP1, S7) = -1

        int class1No = getSPClassNumber(class1.getName());
        LOG.debug("Class no for '{}' is {}", class1.getName(), class1No);
        
        if (class1No < 0) {
            throw new IllegalArgumentException("Cannot compare non-sports performance vehicle.");
        }

        int class2No = getSPClassNumber(class2.getName());
        LOG.debug("Class no for '{}' is {}", class2.getName(), class2No);
        if (class2No > 0) {
            // Both SPs
            return class2No - class1No;
        }
        
        class2No = getSClassNumber(class2.getName());
        // If class 2 not S class, we can't compare
        if (class2No < 0) {
            throw new IllegalArgumentException("Cannot compare sports performance vehicle to class " + class2.getName() + ".");
        }

        // Different classes - we are only considering SP and S classes
        return -class1No - (7-class2No); // assunimg best S-class is S7
    }
    
    static private int getSPClassNumber(String vclassName) {
        if (vclassName == null || vclassName.length() < 3 || !vclassName.substring(0, 2).equals("SP")) {
            return -1;
        }
    
        String className;
        
        if (vclassName.endsWith("ESTA")) {
            className = vclassName.substring(0, vclassName.length()-4);
        } else if (vclassName.endsWith("EST")) {
            className = vclassName.substring(0, vclassName.length()-3);
        } else if (vclassName.endsWith("A")) {
            className = vclassName.substring(0, vclassName.length()-1);
        } else {
            className = vclassName;
        }

        LOG.debug ("Getting SP class number for {}", className);
        
        if (className.length() == 3) {
            return Integer.parseInt(className.substring(2));
        }
        else if (className.length() == 4 && className.charAt(3) >= '0' && className.charAt(3) <= '9') {
            return Integer.parseInt(className.substring(2));
        }
        else if (className.length() == 4) {
            return Integer.parseInt(className.substring(2, 1));
        }
        else if (className.charAt(3) >= '0' && className.charAt(3) <= '9') {
            return Integer.parseInt(className.substring(2, 2));
        }
        else {
            return Integer.parseInt(className.substring(2, 1));
        }
    }
    
    static private int getPClassNumber(String vclassName) {
        if (vclassName == null || vclassName.length() < 2 || vclassName.charAt(0) != 'P'
                || !(vclassName.charAt(1) >= '1' && vclassName.charAt(1) <= '9')) {
            return -1;
        }
    
        String className;
        
        if (vclassName.endsWith("ESTA")) {
            className = vclassName.substring(0, vclassName.length()-4);
        } else if (vclassName.endsWith("EST")) {
            className = vclassName.substring(0, vclassName.length()-3);
        } else if (vclassName.endsWith("A")) {
            className = vclassName.substring(0, vclassName.length()-1);
        } else {
            className = vclassName;
        }

        LOG.debug ("Getting P class number for {}", className);

        if (className.length() == 2) {
            return Integer.parseInt(className.substring(1));
        }
        else if (className.length() == 3 && className.charAt(2) >= '0' && className.charAt(2) <= '9') {
            return Integer.parseInt(className.substring(1));
        }
        else if (className.length() == 3) {
            return Integer.parseInt(className.substring(1, 1));
        }
        else if (className.charAt(2) >= '0' && className.charAt(3) <= '9') {
            return Integer.parseInt(className.substring(1, 2));
        }
        else {
            return Integer.parseInt(className.substring(1, 1));
        }
    }
    
    static private int getSClassNumber(String vclassName) {
        if (vclassName == null || vclassName.length() < 2 || vclassName.charAt(0) != 'S'
                || !(vclassName.charAt(1) >= '1' && vclassName.charAt(1) <= '9')) {
            return -1;
        }

        String className;
        
        if (vclassName.endsWith("ESTA")) {
            className = vclassName.substring(0, vclassName.length()-4);
        } else if (vclassName.endsWith("EST")) {
            className = vclassName.substring(0, vclassName.length()-3);
        } else if (vclassName.endsWith("A")) {
            className = vclassName.substring(0, vclassName.length()-1);
        } else {
            className = vclassName;
        }

        if (className.length() == 2) {
            return Integer.parseInt(className.substring(1));
        }
        else if (className.length() == 3 && className.charAt(2) >= '0' && className.charAt(2) <= '9') {
            return Integer.parseInt(className.substring(1));
        }
        else if (className.length() == 3) {
            return Integer.parseInt(className.substring(1, 1));
        }
        else if (className.charAt(2) >= '0' && className.charAt(3) <= '9') {
            return Integer.parseInt(className.substring(1, 2));
        }
        else {
            return Integer.parseInt(className.substring(1, 1));
        }
    }
    
}
