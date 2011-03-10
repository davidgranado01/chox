/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.core.model;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author rajareddydodda
 */
public class AutomaticRoutingPrice extends Entity implements Serializable{
    private Insurer insurer;
    private Workgroup workgroup;
    private BigDecimal price;

    /**
     * @return the insurer
     */
    public Insurer getInsurer() {
        return insurer;
    }

    /**
     * @param insurer the insurer to set
     */
    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    /**
     * @return the workgroup
     */
    public Workgroup getWorkgroup() {
        return workgroup;
    }

    /**
     * @param workgroup the workgroup to set
     */
    public void setWorkgroup(Workgroup workgroup) {
        this.workgroup = workgroup;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    

}
