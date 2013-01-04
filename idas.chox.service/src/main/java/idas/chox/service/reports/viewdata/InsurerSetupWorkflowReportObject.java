package idas.chox.service.reports.viewdata;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InsurerSetupWorkflowReportObject {
    
    private static final Logger LOG = LoggerFactory.getLogger(InsurerSetupWorkflowReportObject.class);
    private String status;
    private List<InsurerSetupWorkflowLineItem> lineItems;

    public InsurerSetupWorkflowReportObject(String status) {
        this.status = status;
        lineItems = new ArrayList<InsurerSetupWorkflowLineItem>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<InsurerSetupWorkflowLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<InsurerSetupWorkflowLineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
