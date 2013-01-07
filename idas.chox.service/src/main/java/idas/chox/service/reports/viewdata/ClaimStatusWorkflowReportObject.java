package idas.chox.service.reports.viewdata;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClaimStatusWorkflowReportObject {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClaimStatusWorkflowReportObject.class);
    private String status;
    private List<ClaimStatusWorkflowLineItem> lineItems;

    public ClaimStatusWorkflowReportObject(String status) {
        this.status = status;
        lineItems = new ArrayList<ClaimStatusWorkflowLineItem>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<ClaimStatusWorkflowLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<ClaimStatusWorkflowLineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
