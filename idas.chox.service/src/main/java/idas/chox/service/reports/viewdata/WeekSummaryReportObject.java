/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.reports.viewdata;

import java.util.Date;

/**
 *
 * @author Emmanuel
 */
public class WeekSummaryReportObject {
    private Date weekCycleFrom;
    private Date weekCycleTo;
    private Date createdDate;

    public Date getWeekCycleFrom() {
        return weekCycleFrom;
    }

    public void setWeekCycleFrom(Date weekCycleFrom) {
        this.weekCycleFrom = weekCycleFrom;
    }

    public Date getWeekCycleTo() {
        return weekCycleTo;
    }

    public void setWeekCycleTo(Date weekCycleTo) {
        this.weekCycleTo = weekCycleTo;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
