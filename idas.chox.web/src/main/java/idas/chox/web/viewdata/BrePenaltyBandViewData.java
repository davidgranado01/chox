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
    private boolean useCommercialDay1;
    private String useCommercialDescDay1;
    private boolean useCommercialDay2;
    private String useCommercialDescDay2;
    private boolean useCommercialDay3;
    private String useCommercialDescDay3;
    private int hirePeriodStartDay1;
    private int hirePeriodStartDay2;
    private int hirePeriodStartDay3;
    private BigDecimal repairDayRate1;
    private BigDecimal repairDayRate2;
    private BigDecimal repairDayRate3;
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
        this.useCommercialDay1 = object.isUseCommercialDay1();
        this.useCommercialDay2 = object.isUseCommercialDay2();
        this.useCommercialDay3 = object.isUseCommercialDay3();
        this.useCommercialDescDay1 = useCommercialDay1 ? "Yes" : "No";
        this.useCommercialDescDay2 = useCommercialDay2 ? "Yes" : "No";
        this.useCommercialDescDay3 = useCommercialDay3 ? "Yes" : "No";
        this.hirePeriodStartDay1 = object.getHirePeriodStartDay1();
        this.hirePeriodStartDay2 = object.getHirePeriodStartDay2();
        this.hirePeriodStartDay3 = object.getHirePeriodStartDay3();
        if (hirePeriodStartDay1 == -1) {
            this.hireWindow1 = "";
        } else if (this.useCommercialDay1) {
            this.hireWindow1 = hirePeriodStartDay1 + " C";
        } else {
            this.hireWindow1 = hirePeriodStartDay1 + " " + hireDayRate1.toString() + "%";
        }
        if (hirePeriodStartDay2 == -1) {
            this.hireWindow2 = "";
        } else if (this.useCommercialDay2) {
            this.hireWindow2 = hirePeriodStartDay2 + " C";
        } else {
            this.hireWindow2 = hirePeriodStartDay2 + " " + hireDayRate2.toString() + "%";
        }
        if (hirePeriodStartDay3 == -1) {
            this.hireWindow3 = "";
        } else if (this.useCommercialDay3) {
            this.hireWindow3 = hirePeriodStartDay3 + " C";
        } else {
            this.hireWindow3 = hirePeriodStartDay3 + " " + hireDayRate3.toString() + "%";
        }
        this.repairDayRate1 = object.getRepairDay1();
        this.repairDayRate2 = object.getRepairDay2();
        this.repairDayRate3 = object.getRepairDay3();
        if (repairPeriodStartDay1 == -1) {
            this.repairWindow1 = "";
        } else if (this.useCommercialDay1) {
            this.repairWindow1 = repairPeriodStartDay1 + " C";
        } else {
            this.repairWindow1 = repairPeriodStartDay1 + " " + repairDayRate1.toString() + "%";
        }
        if (repairPeriodStartDay2 == -1) {
            this.repairWindow2 = "";
        } else if (this.useCommercialDay2) {
            this.repairWindow2 = repairPeriodStartDay2 + " C";
        } else {
            this.repairWindow2 = repairPeriodStartDay2 + " " + repairDayRate2.toString() + "%";
        }
        if (repairPeriodStartDay3 == -1) {
            this.repairWindow3 = "";
        } else if (this.useCommercialDay3) {
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

    public boolean isUseCommercialDay1() {
        return useCommercialDay1;
    }

    public void setUseCommercialDay1(boolean useCommercialDay1) {
        this.useCommercialDay1 = useCommercialDay1;
    }

    public String getUseCommercialDescDay1() {
        return useCommercialDescDay1;
    }

    public void setUseCommercialDescDay1(String useCommercialDescDay1) {
        this.useCommercialDescDay1 = useCommercialDescDay1;
    }

    public boolean isUseCommercialDay2() {
        return useCommercialDay2;
    }

    public void setUseCommercialDay2(boolean useCommercialDay2) {
        this.useCommercialDay2 = useCommercialDay2;
    }

    public String getUseCommercialDescDay2() {
        return useCommercialDescDay2;
    }

    public void setUseCommercialDescDay2(String useCommercialDescDay2) {
        this.useCommercialDescDay2 = useCommercialDescDay2;
    }

    public boolean isUseCommercialDay3() {
        return useCommercialDay3;
    }

    public void setUseCommercialDay3(boolean useCommercialDay3) {
        this.useCommercialDay3 = useCommercialDay3;
    }

    public String getUseCommercialDescDay3() {
        return useCommercialDescDay3;
    }

    public void setUseCommercialDescDay3(String useCommercialDescDay3) {
        this.useCommercialDescDay3 = useCommercialDescDay3;
    }

}
