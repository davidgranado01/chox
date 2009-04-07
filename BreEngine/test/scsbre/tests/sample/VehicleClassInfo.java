package scsbre.tests.sample;

import java.math.BigDecimal;

import scsbre.model.IVehicleClassInfo;

public class VehicleClassInfo implements IVehicleClassInfo {

    private String code;
    private BigDecimal price;

    /* (non-Javadoc)
     * @see scsbre.model.IVehicleClassInfo#getCode()
     */
    public String getCode() {
        return code;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IVehicleClassInfo#setCode(java.lang.String)
     */

    public void setCode(String code) {
        this.code = code;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IVehicleClassInfo#getPrice()
     */

    public BigDecimal getPrice() {
        return price;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IVehicleClassInfo#setPrice(java.math.BigDecimal)
     */

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
