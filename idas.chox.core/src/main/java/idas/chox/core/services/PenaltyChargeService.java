
package idas.chox.core.services;

import idas.chox.core.model.Claim;
import java.util.Date;
import java.util.List;

import idas.chox.core.model.Invoice;
import idas.chox.core.model.PenaltyCharge;
import java.math.BigDecimal;


public interface PenaltyChargeService {
   
    public List<PenaltyCharge> getHirePenaltyPercentages(Date hireStart, int penaltyType);
    
    public List<PenaltyCharge> getRepairPenaltyPercentages(int penaltyType);
    
    public String getHirePenaltyPercentage(Date hireStart, Invoice inv, int penaltyType);
    
    public String getRepairPenaltyPercentage(Invoice inv, int penaltyType);
    
    public boolean isAppliedHirePenaltyPercentageDifferent(Date hireStart, Invoice inv, int penaltyType);
    
    public boolean isAppliedRepairPenaltyPercentageDifferent(Invoice inv, int penaltyType);
    
    public BigDecimal calculateHirePenaltyCharge(Invoice inv, String hirePercentage, Date hireStart, int penaltyType);
    
    public BigDecimal calculateHirePenaltyCharge(Claim claim);
    
    public BigDecimal calculateRepairPenaltyCharge(Invoice inv, String repairPercentage, int penaltyType);
    
    public BigDecimal calculateRepairPenaltyCharge(Claim claim);
    
}
