package idas.chox.data;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author John
 */
public class ExcelComment {

    String choReference;
    String createdBy;
    Date createdDate;
    String comment;
    int visibilityType;
    
    public ExcelComment(Map data) {
        choReference = (String) data.get("choreference");
        createdBy = (String) data.get("createdby");
        createdDate = (Date) data.get("createddate");
        comment = StringEscapeUtils.unescapeHtml((String) data.get("comment"));
        visibilityType = (Integer) data.get("visibilitytype");
    }

    public String getChoReference() {
        return choReference;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public int getVisibilityType() {
        return visibilityType;
    }
}
