package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author John
 */
public class AdminFee extends Entity implements Serializable, FullAudit {
    private BigDecimal fee;
    private Date startDate;
    private boolean managingRepair;
    private boolean coverNoteRequired;

    public boolean isCoverNoteRequired() {
        return coverNoteRequired;
    }

    public void setCoverNoteRequired(boolean coverNoteRequired) {
        this.coverNoteRequired = coverNoteRequired;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public boolean isManagingRepair() {
        return managingRepair;
    }

    public void setManagingRepair(boolean managingRepair) {
        this.managingRepair = managingRepair;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

}
