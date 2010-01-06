package chox.model;

import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import java.io.Serializable;
import java.util.Date;

public class History extends AuditableEntity implements Serializable {

    /**
     * This attribute maps to the column id in the history table.
     */
    protected String ruleId;
    protected boolean isSystem;
    protected Claim claim;
    /**
     * This attribute maps to the column narrative in the history table.
     */
    protected String narrative;
    /**
     * This attribute maps to the column process_date in the history table.
     */
    protected Date processDate;
    /**
     * This attribute maps to the column is_public in the history table.
     */
    protected boolean isPublic;
    /**
     * This attribute maps to the column type in the history table.
     */
    protected String type;

    /**
     * Method 'History'
     *
     */
    public History() {
    }

    /**
     * Method 'getNarrative'
     *
     * @return java.lang.String
     */
    public java.lang.String getNarrative() {
        return narrative;
    }

    /**
     * Method 'setNarrative'
     *
     * @param narrative
     */
    public void setNarrative(java.lang.String narrative) {
        this.narrative = narrative;
    }

    /**
     * Method 'getProcessDate'
     *
     * @return java.util.Date
     */
    @TypeConversion(converter = "chox.data.DateConverter")
    public java.util.Date getProcessDate() {
        return processDate;
    }

    /**
     * Method 'setProcessDate'
     *
     * @param processDate
     */
    @TypeConversion(converter = "chox.data.DateConverter")
    public void setProcessDate(java.util.Date processDate) {
        this.processDate = processDate;
    }

    /**
     * Method 'getIsPublic'
     *
     * @return short
     */
    public boolean getIsPublic() {
        return isPublic;
    }

    /**
     * Method 'setIsPublic'
     *
     * @param isPublic
     */
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * Method 'getType'
     *
     * @return java.lang.String
     */
    public java.lang.String getType() {
        return type;
    }

    /**
     * Method 'setType'
     *
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public boolean isIsSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }
}
