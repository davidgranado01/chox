package idas.chox.core.model;

import idas.chox.core.util.DateHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public enum PenaltyPercentage {
    /*
     * Same percentages should apply to the applyautopenaltycharge stored procedure!
     */
    ZERO_PERCENTAGE              ("0%",BigDecimal.ZERO),
    HIRE_MORE_THAN_30_DAYS_AFTER_15_JUNE_2012   ("12.5%",new BigDecimal(12.5)),
    HIRE_MORE_THAN_60_DAYS_AFTER_15_JUNE_2012   ("20%",new BigDecimal(20)),
    HIRE_MORE_THAN_30_DAYS_BEFORE_15_JUNE_2012  ("7.5%",new BigDecimal(7.5)),
    HIRE_MORE_THAN_60_DAYS_BEFORE_15_JUNE_2012  ("15%",new BigDecimal(15)),
    COMMERCIAL                   ("Commercial",BigDecimal.ZERO),
    REPAIR_MORE_THAN_30_DAYS     ("2.5%",new BigDecimal(2.5)),
    REPAIR_MORE_THAN_60_DAYS     ("5%",new BigDecimal(5));

    public static final String PENALTY_INCREASE_DATE = "15/06/2012";
    private String percentage;
    private BigDecimal percentageValue;
    
    PenaltyPercentage(String percentage, BigDecimal percentageValue) {
        this.percentage = percentage;
        this.percentageValue = percentageValue;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getPercentageValue() {
        return percentageValue;
    }

    public void setPercentageValue(BigDecimal percentageValue) {
        this.percentageValue = percentageValue;
    }
    
    public static List<PenaltyPercentage> getHirePenaltyPercentage(Date hireStart){
        List <PenaltyPercentage> hirePenaltyPercentage = new ArrayList<PenaltyPercentage>();
        if(DateHelper.Parse(PENALTY_INCREASE_DATE).compareTo(hireStart) <= 0){
            hirePenaltyPercentage.add(HIRE_MORE_THAN_30_DAYS_AFTER_15_JUNE_2012);
            hirePenaltyPercentage.add(HIRE_MORE_THAN_60_DAYS_AFTER_15_JUNE_2012);
        } else {
            hirePenaltyPercentage.add(HIRE_MORE_THAN_30_DAYS_BEFORE_15_JUNE_2012);
            hirePenaltyPercentage.add(HIRE_MORE_THAN_60_DAYS_BEFORE_15_JUNE_2012);
        }
        hirePenaltyPercentage.add(COMMERCIAL);
        return hirePenaltyPercentage;
    }
    
    public static List<PenaltyPercentage> getRepairPenaltyPercentage(){
        List <PenaltyPercentage> repairPenaltyPercentage = new ArrayList<PenaltyPercentage>();
        repairPenaltyPercentage.add(REPAIR_MORE_THAN_30_DAYS);
        repairPenaltyPercentage.add(REPAIR_MORE_THAN_60_DAYS);
        return repairPenaltyPercentage;
    }

} 
