/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.core.model;

import java.math.BigDecimal;

/**
 *
 * @author abrar
 */
public class BillingChoRate extends Entity {
   
    private Chorganisation chorganisation;
    private Integer minVolume;
    private Integer maxVolume;
    private BigDecimal fee;



    /**
     * @return the chorganisation
     */
    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    /**
     * @param chorganisation the chorganisation to set
     */
    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }

    /**
     * @return the minVolume
     */
    public Integer getMinVolume() {
        return minVolume;
    }

    /**
     * @param minVolume the minVolume to set
     */
    public void setMinVolume(Integer minVolume) {
        this.minVolume = minVolume;
    }

    /**
     * @return the maxVolume
     */
    public Integer getMaxVolume() {
        return maxVolume;
    }

    /**
     * @param maxVolume the maxVolume to set
     */
    public void setMaxVolume(Integer maxVolume) {
        this.maxVolume = maxVolume;
    }

    /**
     * @return the fee
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * @param fee the fee to set
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
