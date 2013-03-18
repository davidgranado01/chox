
package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import idas.chox.core.model.Claim;
import idas.chox.core.model.PenaltyCharge;
import static idas.chox.core.model.PenaltyCharge.*;


public interface PenaltyChargeService {
   
    List<PenaltyCharge> getPenaltyCharges(Date hireStart, PenaltyType penaltyType, PenaltyName penaltyName);
    
    PenaltyCharge getPenaltyCharge(Date hireStart, int penaltyAge, PenaltyType penaltyType, PenaltyName penaltyName);
     
    String getPenaltyPercentageDsc(Claim claim, PenaltyName penaltyName);
    
    BigDecimal getPenaltyPercentageVal(Claim claim, PenaltyName penaltyName);
             
    BigDecimal calculatePenaltyChargeVal(Claim claim, String Percentage, PenaltyName penaltyName);
    
    BigDecimal calculatePenaltyChargeVal(Claim claim, PenaltyName penaltyName);
    
    int getNextPenaltyBand(Claim claim);
    
    int getFirstPenaltyBand(Claim claim);
            
    int getLastPenaltyBand(Claim claim);
    
    int calculateCurrentPenaltyBand(Claim claim);
    
    boolean setPenaltyStartToDateInvoiced(String choReference);
    
    Map adjustAutoPenaltyCharge(Claim claim, Date autoPenaltyStart, boolean isCHO);
            
    Map applyPenaltyCharge(Claim claim, Boolean isPenaltyAlertNotUsed, BigDecimal hirePenaltyChargeAmount, 
            String hirePenaltyPercentage, BigDecimal repairPenaltyChargeAmount, String repairPenaltyPercentage);
    
    boolean canShowPenaltyChargeAlert(Claim claim, boolean isCHO);
}
