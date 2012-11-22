
package idas.chox.core.services;

import idas.chox.core.model.Claim;
import java.util.Date;
import java.util.List;

import idas.chox.core.model.Invoice;
import idas.chox.core.model.PenaltyCharge;
import idas.chox.core.model.PenaltyCharge.PenaltyType;
import java.math.BigDecimal;


public interface PenaltyChargeService {
   
    public List<PenaltyCharge> getHirePenaltyPercentages(Date hireStart, PenaltyType penaltyType);
    
    public List<PenaltyCharge> getRepairPenaltyPercentages(PenaltyType penaltyType);
    
    public String getHirePenaltyPercentage(Date hireStart, Invoice inv, PenaltyType penaltyType);
    
    public String getRepairPenaltyPercentage(Invoice inv, PenaltyType penaltyType);
    
    public BigDecimal calculateHirePenaltyCharge(Invoice inv, String hirePercentage, Date hireStart, PenaltyType penaltyType);
    
    public BigDecimal calculateHirePenaltyCharge(Claim claim);
    
    public BigDecimal calculateRepairPenaltyCharge(Invoice inv, String repairPercentage, PenaltyType penaltyType);
    
    public BigDecimal calculateRepairPenaltyCharge(Claim claim);
    
}
