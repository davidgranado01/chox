package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class VehicleClass extends Entity implements Serializable {

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
        else if(className.charAt(0) == 'P' && className.charAt(1) == 'T'  && className.charAt(2) >= '1' && className.charAt(2) <= '9') {
            return true;
        }

        return false;
    }
    static public boolean isPTClass(String className) {
        if(className.charAt(0) == 'P' && className.charAt(1) == 'T'  && className.charAt(2) >= '1' && className.charAt(2) <= '9') {
            return true;
        }

        return false;
    }
    static public int classPDifference(VehicleClass class1, VehicleClass class2) {
        // if class1 < (i.e. is cheaper thsn) class2 then return a positive number
        // indicating the difference between the two class types,
        // e.g. classDifference(P3, P5) = 2
        //      classDifference(P5, P3) = -2
        //      classDifference(P1, P1) = 0
        int unknown = -999;

        if (class1.getName().charAt(0) != 'P' || !(class1.getName().charAt(1) >= '1' && class1.getName().charAt(1) <= '9'))
            throw new IllegalArgumentException("Cannot compare non-prestige vehicle.");

        if ((class2.getName().charAt(0) != 'P' && class2.getName().charAt(0) != 'S') || !(class2.getName().charAt(1) >= '1' && class2.getName().charAt(1) <= '9'))
            throw new IllegalArgumentException("Cannot compare prestige vehicle to class " + class2.getName() + ".");

        if (class1.getName().equals(class2.getName()))
            return 0;

        if (class1.getName().charAt(0) == class2.getName().charAt(0)
                && class1.getName().charAt(1) >= '1' && class1.getName().charAt(1) <= '9'
                && class2.getName().charAt(1) >= '1' && class2.getName().charAt(1) <= '9') {
            // Both of same class with different numbers
            return Integer.parseInt(class2.getName().substring(1)) - Integer.parseInt(class1.getName().substring(1));
        }

/* Not needed as we are only interested in P-class
        if (class1.getName().charAt(0) == class2.getName().charAt(0)
                && class1.getName().charAt(1) == class2.getName().charAt(1)
                && class1.getName().charAt(2) >= '1' && class1.getName().charAt(2) <= '9'
                && class2.getName().charAt(2) >= '1' && class2.getName().charAt(2) <= '9') {
            // Both of same class with different numbers
            return Integer.parseInt(class2.getName().substring(2)) - Integer.parseInt(class1.getName().substring(2));
        }
*/

        // Different classes - we are only considering P and S classes
        int class1Number = Integer.parseInt(class1.getName().substring(1));
        int class2Number = Integer.parseInt(class2.getName().substring(1));
        return -class1Number - (7-class2Number); // assunimg best S-class is S7

    }
}
