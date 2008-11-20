package scsbre.engine.util;

import java.math.BigDecimal;
import java.util.Date;

import scsbre.model.IClaimInfo;

public class ClaimCalcHelper {
	
	
	private IClaimInfo claim;
        private ExtrasCalcHelper exCalcHelper;
	
	private ClaimCalcHelper(){
		
		
	}
	
	public static ClaimCalcHelper Create(IClaimInfo c)
	{
		ClaimCalcHelper cc = new ClaimCalcHelper();
		cc.claim = c;
                cc.exCalcHelper = ExtrasCalcHelper.Create(c.getClaimExtras());
		return cc;		
	}
	
	
	/*------------- helper calc methods ---------------------------*/


	public int getHireDuration()
	{
		Date hireStart = claim.getClaimHireDetail().getHireStart();
		Date initialEcd = claim.getClaimCustomerVehicleDamage().getInitialECD();
		return CalcHelper.getDaysBetweenDates(hireStart, initialEcd);
	}

	public BigDecimal getDailyHireRateCharged()
	{
		BigDecimal hireNetMinusExtras = claim.getClaimInvoice().getHireNet().subtract(exCalcHelper.getTotalExtras());
		return hireNetMinusExtras.divide(new BigDecimal(claim.getClaimHireDetail().getNumberOfHireDays()));
	}

	public BigDecimal getDailyHireRateChargedWithToleranceDeduction()
	{
		BigDecimal tolerance = getDailyHireRateCharged().multiply(claim.getClaimChoBand().getHireRateChargeTolerance());
		return getDailyHireRateCharged().subtract(tolerance);
	}

	public int getAllowedDays()
	{
		int allowedDays = 0;

		allowedDays += claim.getClaimChoBand().getWeekendBufferDays();
		allowedDays += claim.getClaimChoBand().getTakeVehicleOutDays();
		allowedDays += claim.getClaimChoBand().getEngineerInspectionDelayDays();

		if (claim.getClaimCustomerVehicleDamage().getInitialECD() == null) //no ecd
		{
			if (claim.getClaimCustomerVehicleDamage().getIsUsable())
			{
				allowedDays += claim.getClaimChoBand().getTakeVehicleToGarageDaysMobile();
				allowedDays += claim.getClaimChoBand().getIsMobileDayAllowance();
			}
			else
			{
				allowedDays += claim.getClaimChoBand().getTakeVehicleToGarageDaysNonMobile();
				allowedDays += claim.getClaimChoBand().getIsNotMobileDayAllowance();
			}
		}
		else //we have an ecd
		{
			allowedDays += getHireDuration() + 1;
			if (claim.getClaimCustomerVehicleDamage().getIsUsable())
			{
				allowedDays += claim.getClaimChoBand().getTakeVehicleToGarageDaysMobile();
			}
			else
			{
				allowedDays += claim.getClaimChoBand().getTakeVehicleToGarageDaysNonMobile();
			}
		}
		return allowedDays;
	}   

}
