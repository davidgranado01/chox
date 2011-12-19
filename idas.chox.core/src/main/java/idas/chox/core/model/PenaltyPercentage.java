package idas.chox.core.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public enum PenaltyPercentage {
    
    NO_VALUE                     ("0%",BigDecimal.ZERO),
    HIRE_MORE_THAN_30_DAYS       ("7.5%",new BigDecimal(7.5)),
    HIRE_MORE_THAN_60_DAYS       ("15%",new BigDecimal(15)),
    HIRE_COMMERCIAL              ("Commercial",BigDecimal.ZERO),
    REPAIR_MORE_THAN_30_DAYS     ("2.5%",new BigDecimal(2.5)),
    REPAIR_MORE_THAN_60_DAYS     ("5%",new BigDecimal(5));

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
    
    public static List<PenaltyPercentage> getHirePenaltyPercentage(){
        List <PenaltyPercentage> hirePenaltyPercentage = new ArrayList<PenaltyPercentage>();
        hirePenaltyPercentage.add(HIRE_MORE_THAN_30_DAYS);
        hirePenaltyPercentage.add(HIRE_MORE_THAN_60_DAYS);
        hirePenaltyPercentage.add(HIRE_COMMERCIAL);
        return hirePenaltyPercentage;
    }
    
    public static List<PenaltyPercentage> getRepairPenaltyPercentage(){
        List <PenaltyPercentage> repairPenaltyPercentage = new ArrayList<PenaltyPercentage>();
        repairPenaltyPercentage.add(REPAIR_MORE_THAN_30_DAYS);
        repairPenaltyPercentage.add(REPAIR_MORE_THAN_60_DAYS);
        return repairPenaltyPercentage;
    }
} 
