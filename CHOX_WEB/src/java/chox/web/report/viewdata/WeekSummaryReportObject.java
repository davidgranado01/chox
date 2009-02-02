/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report.viewdata;

/**
 *
 * @author Emmanuel
 */
public class WeekSummaryReportObject {
    private String weekCycleFrom;
    private String weekCycleTo;
    private String name;
    private String createdDate;

    public String getWeekCycleFrom() {
        return weekCycleFrom;
    }

    public void setWeekCycleFrom(String weekCycleFrom) {
        this.weekCycleFrom = weekCycleFrom;
    }

    public String getWeekCycleTo() {
        return weekCycleTo;
    }

    public void setWeekCycleTo(String weekCycleTo) {
        this.weekCycleTo = weekCycleTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
