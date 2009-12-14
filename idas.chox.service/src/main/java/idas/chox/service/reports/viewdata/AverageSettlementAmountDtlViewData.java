/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;

/**
 *
 * @author Carlson
 */
public class AverageSettlementAmountDtlViewData {
    
    private int chorganisationId;
    private BigDecimal value = new BigDecimal("0.00");
    private BigDecimal totalToPay = new BigDecimal("0.00");
    private int recortCount = 0;

    public BigDecimal getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(BigDecimal totalToPay) {
        this.totalToPay = totalToPay;
    }

    public int getRecortCount() {
        return recortCount;
    }

    public void setRecortCount(int recortCount) {
        this.recortCount = recortCount;
    }

    public int getChorganisationId() {
        return chorganisationId;
    }

    public void setChorganisationId(int chorganisationId) {
        this.chorganisationId = chorganisationId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
