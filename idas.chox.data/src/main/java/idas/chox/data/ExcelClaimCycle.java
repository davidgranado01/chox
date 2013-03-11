package idas.chox.data;

import java.util.Date;
import java.util.Map;

import idas.chox.core.util.DateHelper;


/**
 *
 * @author seeni
 */
public class ExcelClaimCycle {

    private String choReference;
    private String modifiedDate;
    private String modifiedBy;
    private String status;
    private String reverted;

    public ExcelClaimCycle(Map data) {
        choReference = (String) data.get("choreference");
        modifiedDate = DateHelper.getLocalDateTimeFormat().format((Date) data.get("modifieddate"));
        modifiedBy = (String) data.get("modifiedby");
        status = (String) data.get("status");
        Boolean rev = (Boolean) data.get("reverted");
        if (rev == null) {
            reverted = "";
        }
        else {
            reverted = rev ? "Yes" : "";
        }        
    }

    public String getChoReference() {
        return choReference;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

     public String getReverted() {
        return reverted;
    }

    public String getStatus() {
        return status;
    }
}
