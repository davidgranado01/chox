package idas.chox.core.model;

import java.math.BigDecimal;
import java.util.Date;

public class PenaltyCharge {

    private int id;
    private PenaltyType penaltyType;
    private int penaltyStartAge;
    private Date penaltyStartDate;
    private String hirePenaltyPercentageDsc;
    private BigDecimal hirePenaltyPercentageVal;
    private String repairPenaltyPercentageDsc;
    private BigDecimal repairPenaltyPercentageVal;
   
    public enum PenaltyType {
        
        DEFAULT ,
        SUBSCRIBER;
    }

    public enum PenaltyName {
        HIRE,
        REPAIR;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PenaltyType getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(PenaltyType penaltyType) {
        this.penaltyType = penaltyType;
    }

    public int getPenaltyStartAge() {
        return penaltyStartAge;
    }

    public void setPenaltyStartAge(int penaltyStartAge) {
        this.penaltyStartAge = penaltyStartAge;
    }

    public Date getPenaltyStartDate() {
        return penaltyStartDate;
    }

    public void setPenaltyStartDate(Date penaltyStartDate) {
        this.penaltyStartDate = penaltyStartDate;
    }

    public String getHirePenaltyPercentageDsc() {
        return hirePenaltyPercentageDsc;
    }

    public void setHirePenaltyPercentageDsc(String hirePenaltyPercentageDsc) {
        this.hirePenaltyPercentageDsc = hirePenaltyPercentageDsc;
    }

    public BigDecimal getHirePenaltyPercentageVal() {
        return hirePenaltyPercentageVal;
    }

    public void setHirePenaltyPercentageVal(BigDecimal hirePenaltyPercentageVal) {
        this.hirePenaltyPercentageVal = hirePenaltyPercentageVal;
    }

    public String getRepairPenaltyPercentageDsc() {
        return repairPenaltyPercentageDsc;
    }

    public void setRepairPenaltyPercentageDsc(String repairPenaltyPercentageDsc) {
        this.repairPenaltyPercentageDsc = repairPenaltyPercentageDsc;
    }

    public BigDecimal getRepairPenaltyPercentageVal() {
        return repairPenaltyPercentageVal;
    }

    public void setRepairPenaltyPercentageVal(BigDecimal repairPenaltyPercentageVal) {
        this.repairPenaltyPercentageVal = repairPenaltyPercentageVal;
    }
}
