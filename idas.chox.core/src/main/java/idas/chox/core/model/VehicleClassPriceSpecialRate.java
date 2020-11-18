package idas.chox.core.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


/**
 *
 * @author seeni
 */
public class VehicleClassPriceSpecialRate extends Entity implements Serializable, FullAudit {
    private BigDecimal price;
    private Date startDate;
    private BigDecimal age;
    private VehicleClass vehicleClass;
    private Insurer insurer;
    private Chorganisation chorganisation;

    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public java.math.BigDecimal getPrice() {
        return price;
    }

    public void setPrice(java.math.BigDecimal price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(VehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public BigDecimal getAge() {
        return age;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VehicleClassPriceSpecialRate that = (VehicleClassPriceSpecialRate) o;
        return Objects.equals(price, that.price) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(vehicleClass, that.vehicleClass) &&
                Objects.equals(insurer, that.insurer) &&
                Objects.equals(chorganisation, that.chorganisation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), price, startDate, vehicleClass, insurer, chorganisation);
    }
}

