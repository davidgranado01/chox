package idas.chox.core.model;

import idas.chox.core.util.DateHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public enum PenaltyPercentage {
    
    ZERO_PERCENTAGE              ("0%",BigDecimal.ZERO),
    HIRE_MORE_THAN_30_DAYS_NEW       ("12.5%",new BigDecimal(12.5)),
    HIRE_MORE_THAN_60_DAYS_NEW       ("20%",new BigDecimal(20)),
    HIRE_MORE_THAN_30_DAYS   ("7.5%",new BigDecimal(7.5)),
    HIRE_MORE_THAN_60_DAYS   ("15%",new BigDecimal(15)),
    COMMERCIAL                   ("Commercial",BigDecimal.ZERO),
    REPAIR_MORE_THAN_30_DAYS     ("2.5%",new BigDecimal(2.5)),
    REPAIR_MORE_THAN_60_DAYS     ("5%",new BigDecimal(5));

    public static final String NEW_PENALTY_INCREASE_DATE = "15/06/2012";
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
        if(DateHelper.Parse(NEW_PENALTY_INCREASE_DATE).before(hireStart)){
            hirePenaltyPercentage.add(HIRE_MORE_THAN_30_DAYS_NEW);
            hirePenaltyPercentage.add(HIRE_MORE_THAN_60_DAYS_NEW);
        } else {
            hirePenaltyPercentage.add(HIRE_MORE_THAN_30_DAYS);
            hirePenaltyPercentage.add(HIRE_MORE_THAN_60_DAYS);
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
