package idas.chox.data;

import java.util.Date;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
/**
 *
 * @author John
 */
public class ExcelComment {

    private final String choReference;
    private final String createdBy;
    private final Date createdDate;
    private final String comment;
    private final int visibilityType;
    
    public ExcelComment(Map data) {
        choReference = (String) data.get("choreference");
        createdBy = (String) data.get("createdby");
        createdDate = (Date) data.get("createddate");
        comment = StringEscapeUtils.unescapeHtml4((String) data.get("comment"));
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
