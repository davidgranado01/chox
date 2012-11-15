package idas.chox.core.model;

import java.math.BigDecimal;
import java.util.Date;

public class PenaltyCharge {

    private int id;
    private PenaltyType penaltyType;
    private PenaltyName penaltyName;
    private int penaltyStartAgeFrom;
    private int penaltyStartAgeTo;
    private BigDecimal penaltyPercentage;
    private String penaltyPercentageDsc;
    private Date penaltyStartDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PenaltyName getPenaltyName() {
        return penaltyName;
    }

    public void setPenaltyName(PenaltyName penaltyName) {
        this.penaltyName = penaltyName;
    }

    public PenaltyType getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(PenaltyType penaltyType) {
        this.penaltyType = penaltyType;
    }

    public int getPenaltyStartAgeFrom() {
        return penaltyStartAgeFrom;
    }

    public void setPenaltyStartAgeFrom(int penaltyStartAgeFrom) {
        this.penaltyStartAgeFrom = penaltyStartAgeFrom;
    }

    public int getPenaltyStartAgeTo() {
        return penaltyStartAgeTo;
    }

    public void setPenaltyStartAgeTo(int penaltyStartAgeTo) {
        this.penaltyStartAgeTo = penaltyStartAgeTo;
    }

    public BigDecimal getPenaltyPercentage() {
        return penaltyPercentage;
    }

    public void setPenaltyPercentage(BigDecimal penaltyPercentage) {
        this.penaltyPercentage = penaltyPercentage;
    }

    public String getPenaltyPercentageDsc() {
        return penaltyPercentageDsc;
    }

    public void setPenaltyPercentageDsc(String penaltyPercentageDsc) {
        this.penaltyPercentageDsc = penaltyPercentageDsc;
    }

    public Date getPenaltyStartDate() {
        return penaltyStartDate;
    }

    public void setPenaltyStartDate(Date penaltyStartDate) {
        this.penaltyStartDate = penaltyStartDate;
    }

    public enum ZeroPenaltyPercentage {

        ZERO_PERCENTAGE("0%");
        public String percentageDsc;

        ZeroPenaltyPercentage(String percentage) {
            this.percentageDsc = percentage;
        }

        public String getPercentageDsc() {
            return percentageDsc;
        }
    }
    
    public enum PenaltyName {
        HIRE,
        REPAIR;
    }

    public enum PenaltyType {
        DEFAULT,
        SUBSCRIBER;
    }
}
