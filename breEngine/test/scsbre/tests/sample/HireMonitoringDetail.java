/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.tests.sample;

import java.math.BigDecimal;
import scsbre.model.IHireMonitoringDetail;

public class HireMonitoringDetail implements IHireMonitoringDetail{

    public BigDecimal labourRate;
    public Integer labourHour;
    public BigDecimal labourCost;

    public void setLabourCost(BigDecimal labourCost) {
        this.labourCost = labourCost;
    }

    public void setLabourHour(Integer labourHour) {
        this.labourHour = labourHour;
    }

    public void setLabourRate(BigDecimal labourRate) {
        this.labourRate = labourRate;
    }

    public BigDecimal getLabourRate() {
        return labourRate;
    }

    public Integer getLabourHour() {
        return labourHour;
    }

    public BigDecimal getLabourCost() {
        return labourCost;
    }


}
