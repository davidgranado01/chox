/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.report.viewdata;

import java.util.Date;

/**
 *
 * @author Carlson
 */
public class AverageSettlementAmountReportObject {
    private Date settlementDateFrom;
    private Date settlementDateTo;
    private Date createdDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getSettlementDateFrom() {
        return settlementDateFrom;
    }

    public void setSettlementDateFrom(Date settlementDateFrom) {
        this.settlementDateFrom = settlementDateFrom;
    }

    public Date getSettlementDateTo() {
        return settlementDateTo;
    }

    public void setSettlementDateTo(Date settlementDateTo) {
        this.settlementDateTo = settlementDateTo;
    }
    

}
