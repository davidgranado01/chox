package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class VehicleClass extends Entity implements Serializable {

    /**
     * This attribute maps to the column name in the vehicle_class table.
     */
    protected String name;

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

}
