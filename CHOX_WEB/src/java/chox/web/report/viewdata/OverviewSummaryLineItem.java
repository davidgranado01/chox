/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.report.viewdata;

import java.util.List;

/**
 *
 * @author Carlson
 */
public class OverviewSummaryLineItem {
    
    private int lineId;
    private String name;
    private List<OverviewSummaryLineItemDetail> lineItem;
    
    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public List<OverviewSummaryLineItemDetail> getLineItem() {
        return lineItem;
    }

    public void setLineItem(List<OverviewSummaryLineItemDetail> lineItem) {
        this.lineItem = lineItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
