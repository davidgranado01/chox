/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.web;

import java.math.BigDecimal;

/**
 *
 * @author seeni
 */
public class VehicleClassPriceMapper {
    private String name;
    private BigDecimal price;

    public VehicleClassPriceMapper(String n,BigDecimal p) {
        this.name=n;
        this.price=p;
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
    

}
