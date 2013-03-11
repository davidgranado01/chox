package idas.chox.data;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author seeni
 */
public class ExcelHistory {
    private String choReference;
    private Date processDate;
    private String ruleId;
    private String type;
    private String narrative;
    private Boolean visibleToCHO;

    public ExcelHistory(Map data) {
        choReference = (String) data.get("choreference");
        processDate = (Date) data.get("processdate");
        ruleId = (String) data.get("ruleid");
        type = (String) data.get("type");
        narrative = (String) data.get("narrative");
        visibleToCHO = (Boolean) data.get("ispublic");
    }

    public String getChoReference() {
        return choReference;
    }

    public Boolean isVisibleToCHO() {
        return visibleToCHO;
    }

    public String getNarrative() {
        return narrative;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public String getRuleId() {
        return ruleId;
    }

    public String getType() {
        return type;
    }
    
}
