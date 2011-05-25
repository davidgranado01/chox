package idas.chox.web;

import java.math.BigDecimal;

/**
 *
 * @author seeni
 */
public class VehicleClassPriceMapper {
    private String name;
    private BigDecimal price;
    private BigDecimal price2;

    public VehicleClassPriceMapper(String n,BigDecimal p) {
        this.name=n;
        this.price=p;
        this.price2 = null;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }
    

}
