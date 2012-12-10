
package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import idas.chox.core.model.Claim;
import idas.chox.core.model.PenaltyCharge;
import static idas.chox.core.model.PenaltyCharge.*;


public interface PenaltyChargeService {
   
    public List<PenaltyCharge> getPenaltyCharges(Date hireStart, PenaltyType penaltyType, PenaltyName penaltyName);
    
    public PenaltyCharge getPenaltyCharge(Date hireStart, int penaltyAge, PenaltyType penaltyType, PenaltyName penaltyName);
     
    public String getPenaltyPercentageDsc(Claim claim, PenaltyName penaltyName);
    
    public BigDecimal getPenaltyPercentageVal(Claim claim, PenaltyName penaltyName);
             
    public BigDecimal calculatePenaltyChargeVal(Claim claim, String Percentage, PenaltyName penaltyName);
    
    public BigDecimal calculatePenaltyChargeVal(Claim claim, PenaltyName penaltyName);
    
    public int getNextPenaltyBand(Claim claim);
    
    public int getFirstPenaltyBand(Claim claim);
            
    public int getLastPenaltyBand(Claim claim);
    
    public int calculateCurrentPenaltyBand(Claim claim);
    
    public boolean setPenaltyStartToDateInvoiced(String choReference);
    
    public Map adjustAutoPenaltyCharge(Claim claim, Date autoPenaltyStart);
            
    public Map applyPenaltyCharge(Claim claim, Boolean isPenaltyAlertNotUsed, BigDecimal hirePenaltyChargeAmount, 
            String hirePenaltyPercentage, BigDecimal repairPenaltyChargeAmount, String repairPenaltyPercentage);
    
    public boolean canShowPenaltyChargeAlert(Claim claim, boolean isCHO);
}
