package scsbre.engine.util;

import java.math.BigDecimal;
import java.util.Date;

import scsbre.model.IClaimInfo;

public class ClaimCalcHelper {
	
	
	private IClaimInfo claim;
        private ExtrasCalcHelper exCalcHelper;
	
	private ClaimCalcHelper(){
		
		
	}
	
	public static ClaimCalcHelper getInstance(IClaimInfo c)
	{
		ClaimCalcHelper cc = new ClaimCalcHelper();
		cc.claim = c;
                cc.exCalcHelper = ExtrasCalcHelper.getInstance(c.getExtras());
		return cc;		
	}
	
	
	/*------------- helper calc methods ---------------------------*/
        
        
	public int getHireDuration()
	{
		Date hireStart = claim.getHireDetail().getHireStart();
		//Date initialEcd = claim.getCustomerVehicleDamage().getInitialECD();
                Date initialEcd = claim.getHireMonitoringEcd();
		return CalcHelper.getDaysBetweenDates(hireStart, initialEcd);
	}
        
	public BigDecimal getDailyHireRateCharged()
	{
		BigDecimal hireNetMinusExtras = claim.getInvoice().getHireNet().subtract(exCalcHelper.getTotalExtras());
		return hireNetMinusExtras.divide(new BigDecimal(claim.getHireDetail().getNumberOfHireDays()),4,1);
	}

	public BigDecimal getDailyHireRateChargedWithToleranceDeduction()
	{
		BigDecimal tolerance = getDailyHireRateCharged().multiply(claim.getChoBand().getHireRateChargeTolerance());
		return getDailyHireRateCharged().subtract(tolerance);
	}

	public int getAllowedDays()
	{
		int allowedDays = 0;

		allowedDays += claim.getChoBand().getWeekendBufferDays();
		allowedDays += claim.getChoBand().getTakeVehicleOutDays();
		allowedDays += claim.getChoBand().getEngineerInspectionDelayDays();

		//if (claim.getCustomerVehicleDamage().getInitialECD() == null) //no ecd
                if (claim.getHireMonitoringEcd()==null) //no ecd
		{
			if (claim.getCustomerVehicleDamage().getIsUsable())
			{
				allowedDays += claim.getChoBand().getTakeVehicleToGarageDaysMobile();
				allowedDays += claim.getChoBand().getIsMobileDayAllowance();
			}
			else
			{
				allowedDays += claim.getChoBand().getTakeVehicleToGarageDaysNonMobile();
				allowedDays += claim.getChoBand().getIsNotMobileDayAllowance();
			}
		}
		else //we have an ecd
		{
                    
			allowedDays += getHireDuration() + 1;
			if (claim.getCustomerVehicleDamage().getIsUsable())
			{
				allowedDays += claim.getChoBand().getTakeVehicleToGarageDaysMobile();
			}
			else
			{
				allowedDays += claim.getChoBand().getTakeVehicleToGarageDaysNonMobile();
			}
		}
                
                System.out.println("allowedDays is " + allowedDays);
		return allowedDays;
	}   

}
