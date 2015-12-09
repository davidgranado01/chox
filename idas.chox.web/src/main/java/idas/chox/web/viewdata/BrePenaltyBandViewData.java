package idas.chox.web.viewdata;

import java.math.BigDecimal;

import idas.chox.core.model.BrePenaltyBand;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author John
 */
public class BrePenaltyBandViewData {
    private int id;
    private int claimTypeId;
    private String claimTypeName;
    private String penaltyBandStartDate;
    private BigDecimal hireDayRate1;
    private BigDecimal hireDayRate2;
    private BigDecimal hireDayRate3;
    private boolean hireUseCommercialDay1;
    private String hireUseCommercialDescDay1;
    private boolean hireUseCommercialDay2;
    private String hireUseCommercialDescDay2;
    private boolean hireUseCommercialDay3;
    private String hireUseCommercialDescDay3;
    private int hirePeriodStartDay1;
    private int hirePeriodStartDay2;
    private int hirePeriodStartDay3;
    private BigDecimal repairDayRate1;
    private BigDecimal repairDayRate2;
    private BigDecimal repairDayRate3;
    private boolean repairUseCommercialDay1;
    private String repairUseCommercialDescDay1;
    private boolean repairUseCommercialDay2;
    private String repairUseCommercialDescDay2;
    private boolean repairUseCommercialDay3;
    private String repairUseCommercialDescDay3;
    private int repairPeriodStartDay1;
    private int repairPeriodStartDay2;
    private int repairPeriodStartDay3;
    private String createdBy;
    private String createdDate;
    private boolean removed;
    private String hireWindow1;
    private String hireWindow2;
    private String hireWindow3;
    private String repairWindow1;
    private String repairWindow2;
    private String repairWindow3;
    
    public BrePenaltyBandViewData(){}

    public BrePenaltyBandViewData(BrePenaltyBand object) {
        if (object.getId() != null) {
            this.id = object.getId();
        }
        this.claimTypeId = object.getClaimType().getClaimTypeValue();
        this.claimTypeName = object.getClaimType().toString();
        this.penaltyBandStartDate = DateHelper.getLocalDateFormat().format(object.getStartDate());
        this.hireDayRate1 = object.getHireDay1();
        this.hireDayRate2 = object.getHireDay2();
        this.hireDayRate3 = object.getHireDay3();
        this.hireUseCommercialDay1 = object.isHireUseCommercialDay1();
        this.hireUseCommercialDay2 = object.isHireUseCommercialDay2();
        this.hireUseCommercialDay3 = object.isHireUseCommercialDay3();
        this.hireUseCommercialDescDay1 = hireUseCommercialDay1 ? "Yes" : "No";
        this.hireUseCommercialDescDay2 = hireUseCommercialDay2 ? "Yes" : "No";
        this.hireUseCommercialDescDay3 = hireUseCommercialDay3 ? "Yes" : "No";
        this.hirePeriodStartDay1 = object.getHirePeriodStartDay1();
        this.hirePeriodStartDay2 = object.getHirePeriodStartDay2();
        this.hirePeriodStartDay3 = object.getHirePeriodStartDay3();
        if (hirePeriodStartDay1 == -1) {
            this.hireWindow1 = "";
        } else if (this.hireUseCommercialDay1) {
            this.hireWindow1 = hirePeriodStartDay1 + " C";
        } else {
            this.hireWindow1 = hirePeriodStartDay1 + " " + hireDayRate1.toString() + "%";
        }
        if (hirePeriodStartDay2 == -1) {
            this.hireWindow2 = "";
        } else if (this.hireUseCommercialDay2) {
            this.hireWindow2 = hirePeriodStartDay2 + " C";
        } else {
            this.hireWindow2 = hirePeriodStartDay2 + " " + hireDayRate2.toString() + "%";
        }
        if (hirePeriodStartDay3 == -1) {
            this.hireWindow3 = "";
        } else if (this.hireUseCommercialDay3) {
            this.hireWindow3 = hirePeriodStartDay3 + " C";
        } else {
            this.hireWindow3 = hirePeriodStartDay3 + " " + hireDayRate3.toString() + "%";
        }
        this.repairDayRate1 = object.getRepairDay1();
        this.repairDayRate2 = object.getRepairDay2();
        this.repairDayRate3 = object.getRepairDay3();
        this.repairUseCommercialDay1 = object.isRepairUseCommercialDay1();
        this.repairUseCommercialDescDay1 = repairUseCommercialDay1 ? "Yes" : "No";
        this.repairUseCommercialDay2 = object.isRepairUseCommercialDay2();
        this.repairUseCommercialDescDay2 = repairUseCommercialDay2 ? "Yes" : "No";
        this.repairUseCommercialDay3 = object.isRepairUseCommercialDay3();
        this.repairUseCommercialDescDay3 = repairUseCommercialDay3 ? "Yes" : "No";
        if (repairPeriodStartDay1 == -1) {
            this.repairWindow1 = "";
        } else if (this.repairUseCommercialDay1) {
            this.repairWindow1 = repairPeriodStartDay1 + " C";
        } else {
            this.repairWindow1 = repairPeriodStartDay1 + " " + repairDayRate1.toString() + "%";
        }
        if (repairPeriodStartDay2 == -1) {
            this.repairWindow2 = "";
        } else if (this.repairUseCommercialDay2) {
            this.repairWindow2 = repairPeriodStartDay2 + " C";
        } else {
            this.repairWindow2 = repairPeriodStartDay2 + " " + repairDayRate2.toString() + "%";
        }
        if (repairPeriodStartDay3 == -1) {
            this.repairWindow3 = "";
        } else if (this.repairUseCommercialDay3) {
            this.repairWindow3 = repairPeriodStartDay3 + " C";
        } else {
            this.repairWindow3 = repairPeriodStartDay3 + " " + repairDayRate3.toString() + "%";
        }
        if (object.getCreatedBy() != null) {
            this.createdBy = object.getCreatedBy().getDisplayName();
        }
        if (object.getCreatedDate() != null) {
            this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClaimTypeId() {
        return claimTypeId;
    }

    public void setClaimTypeId(int claimTypeId) {
        this.claimTypeId = claimTypeId;
    }

    public String getClaimTypeName() {
        return claimTypeName;
    }

    public void setClaimTypeName(String claimTypeName) {
        this.claimTypeName = claimTypeName;
    }

    public String getPenaltyBandStartDate() {
        return penaltyBandStartDate;
    }

    public void setPenaltyBandStartDate(String penaltyBandStartDate) {
        this.penaltyBandStartDate = penaltyBandStartDate;
    }

    public BigDecimal getHireDayRate1() {
        return hireDayRate1;
    }

    public void setHireDayRate1(BigDecimal hireDayRate1) {
        this.hireDayRate1 = hireDayRate1;
    }

    public BigDecimal getHireDayRate2() {
        return hireDayRate2;
    }

    public void setHireDayRate2(BigDecimal hireDayRate2) {
        this.hireDayRate2 = hireDayRate2;
    }

    public BigDecimal getHireDayRate3() {
        return hireDayRate3;
    }

    public void setHireDayRate3(BigDecimal hireDayRate3) {
        this.hireDayRate3 = hireDayRate3;
    }


    public boolean isHireUseCommercialDay1() {
        return hireUseCommercialDay1;
    }

    public void setHireUseCommercialDay1(boolean hireUseCommercialDay1) {
        this.hireUseCommercialDay1 = hireUseCommercialDay1;
    }

    public String getHireUseCommercialDescDay1() {
        return hireUseCommercialDescDay1;
    }

    public void setHireUseCommercialDescDay1(String hireUseCommercialDescDay1) {
        this.hireUseCommercialDescDay1 = hireUseCommercialDescDay1;
    }

    public boolean isHireUseCommercialDay2() {
        return hireUseCommercialDay2;
    }

    public void setHireUseCommercialDay2(boolean hireUseCommercialDay2) {
        this.hireUseCommercialDay2 = hireUseCommercialDay2;
    }

    public String getHireUseCommercialDescDay2() {
        return hireUseCommercialDescDay2;
    }

    public void setHireUseCommercialDescDay2(String hireUseCommercialDescDay2) {
        this.hireUseCommercialDescDay2 = hireUseCommercialDescDay2;
    }

    public boolean isHireUseCommercialDay3() {
        return hireUseCommercialDay3;
    }

    public void setHireUseCommercialDay3(boolean hireUseCommercialDay3) {
        this.hireUseCommercialDay3 = hireUseCommercialDay3;
    }

    public String getHireUseCommercialDescDay3() {
        return hireUseCommercialDescDay3;
    }

    public void setHireUseCommercialDescDay3(String hireUseCommercialDescDay3) {
        this.hireUseCommercialDescDay3 = hireUseCommercialDescDay3;
    }

    public BigDecimal getRepairDayRate1() {
        return repairDayRate1;
    }

    public void setRepairDayRate1(BigDecimal repairDayRate1) {
        this.repairDayRate1 = repairDayRate1;
    }

    public BigDecimal getRepairDayRate2() {
        return repairDayRate2;
    }

    public void setRepairDayRate2(BigDecimal repairDayRate2) {
        this.repairDayRate2 = repairDayRate2;
    }

    public BigDecimal getRepairDayRate3() {
        return repairDayRate3;
    }

    public void setRepairDayRate3(BigDecimal repairDayRate3) {
        this.repairDayRate3 = repairDayRate3;
    }

    public boolean isRepairUseCommercialDay1() {
        return repairUseCommercialDay1;
    }

    public void setRepairUseCommercialDay1(boolean repairUseCommercialDay1) {
        this.repairUseCommercialDay1 = repairUseCommercialDay1;
    }

    public String getRepairUseCommercialDescDay1() {
        return repairUseCommercialDescDay1;
    }

    public void setRepairUseCommercialDescDay1(String repairUseCommercialDescDay1) {
        this.repairUseCommercialDescDay1 = repairUseCommercialDescDay1;
    }

    public boolean isRepairUseCommercialDay2() {
        return repairUseCommercialDay2;
    }

    public void setRepairUseCommercialDay2(boolean repairUseCommercialDay2) {
        this.repairUseCommercialDay2 = repairUseCommercialDay2;
    }

    public String getRepairUseCommercialDescDay2() {
        return repairUseCommercialDescDay2;
    }

    public void setRepairUseCommercialDescDay2(String repairUseCommercialDescDay2) {
        this.repairUseCommercialDescDay2 = repairUseCommercialDescDay2;
    }

    public boolean isRepairUseCommercialDay3() {
        return repairUseCommercialDay3;
    }

    public void setRepairUseCommercialDay3(boolean repairUseCommercialDay3) {
        this.repairUseCommercialDay3 = repairUseCommercialDay3;
    }

    public String getRepairUseCommercialDescDay3() {
        return repairUseCommercialDescDay3;
    }

    public void setRepairUseCommercialDescDay3(String repairUseCommercialDescDay3) {
        this.repairUseCommercialDescDay3 = repairUseCommercialDescDay3;
    }

    public int getHirePeriodStartDay1() {
        return hirePeriodStartDay1;
    }

    public void setHirePeriodStartDay1(int hirePeriodStartDay1) {
        this.hirePeriodStartDay1 = hirePeriodStartDay1;
    }

    public int getHirePeriodStartDay2() {
        return hirePeriodStartDay2;
    }

    public void setHirePeriodStartDay2(int hirePeriodStartDay2) {
        this.hirePeriodStartDay2 = hirePeriodStartDay2;
    }

    public int getHirePeriodStartDay3() {
        return hirePeriodStartDay3;
    }

    public void setHirePeriodStartDay3(int hirePeriodStartDay3) {
        this.hirePeriodStartDay3 = hirePeriodStartDay3;
    }

    public int getRepairPeriodStartDay1() {
        return repairPeriodStartDay1;
    }

    public void setRepairPeriodStartDay1(int repairPeriodStartDay1) {
        this.repairPeriodStartDay1 = repairPeriodStartDay1;
    }

    public int getRepairPeriodStartDay2() {
        return repairPeriodStartDay2;
    }

    public void setRepairPeriodStartDay2(int repairPeriodStartDay2) {
        this.repairPeriodStartDay2 = repairPeriodStartDay2;
    }

    public int getRepairPeriodStartDay3() {
        return repairPeriodStartDay3;
    }

    public void setRepairPeriodStartDay3(int repairPeriodStartDay3) {
        this.repairPeriodStartDay3 = repairPeriodStartDay3;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public String getHireWindow1() {
        return hireWindow1;
    }

    public void setHireWindow1(String hireWindow1) {
        this.hireWindow1 = hireWindow1;
    }

    public String getHireWindow2() {
        return hireWindow2;
    }

    public void setHireWindow2(String hireWindow2) {
        this.hireWindow2 = hireWindow2;
    }

    public String getHireWindow3() {
        return hireWindow3;
    }

    public void setHireWindow3(String hireWindow3) {
        this.hireWindow3 = hireWindow3;
    }

    public String getRepairWindow1() {
        return repairWindow1;
    }

    public void setRepairWindow1(String repairWindow1) {
        this.repairWindow1 = repairWindow1;
    }

    public String getRepairWindow2() {
        return repairWindow2;
    }

    public void setRepairWindow2(String repairWindow2) {
        this.repairWindow2 = repairWindow2;
    }

    public String getRepairWindow3() {
        return repairWindow3;
    }

    public void setRepairWindow3(String repairWindow3) {
        this.repairWindow3 = repairWindow3;
    }

}
