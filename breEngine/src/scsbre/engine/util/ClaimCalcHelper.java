package scsbre.engine.util;

import java.math.BigDecimal;
import java.util.Date;
import scsbre.model.IClaimInfo;

public class ClaimCalcHelper {

	private IClaimInfo claim;
        private ExtrasCalcHelper exCalcHelper;
	
	private ClaimCalcHelper(){ }
	
	public static ClaimCalcHelper getInstance(IClaimInfo c)
	{
            ClaimCalcHelper cc = new ClaimCalcHelper();
            cc.claim = c;
            cc.exCalcHelper = ExtrasCalcHelper.getInstance(c.getExtras());
            return cc;		
	}
        
	/*
	public static void main(String[] args) {
            
            DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");

        
		
                
        try {
            Date hireStart = dfm.parse("2008-07-18 00:00:00");
            Date initialEcd = dfm.parse("2008-08-03 00:00:00");
            
                System.out.println(">>>>>>>"+ hireStart);
                System.out.println(">>>>>>>"+ initialEcd);
		System.out.println(">>>>>>>"+ CalcHelper.getDaysBetweenDates(hireStart, initialEcd));
                
        } catch (ParseException ex) {
            Logger.getLogger(ClaimCalcHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        }
        */
        
	/*------------- helper calc methods ---------------------------*/
        
        
	public int getHireDuration()
	{
            
            Date hireStart = claim.getHireDetail().getHireStart();
            Date initialEcd = claim.getHireMonitoringEcd();
            
            // System.out.println("hireStart?:"+hireStart);
            // System.out.println("initialEcd?:"+initialEcd);
            // System.out.println("getDaysBetweenDates?:"+CalcHelper.getDaysBetweenDates(hireStart, initialEcd));
            
            return CalcHelper.getDaysBetweenDates(hireStart, initialEcd);
	}
        
	public BigDecimal getDailyHireRateCharged()
	{
            BigDecimal hireNetMinusExtras = claim.getInvoice().getHireNet().subtract(exCalcHelper.getTotalExtras());
            return hireNetMinusExtras.divide(new BigDecimal(claim.getHireDetail().getNumberOfHireDays()), 4, 1);
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
                
		return allowedDays;
	}   
        
        public int getNumberDayOfLabourCostWorthy(){
            
            int iLabourCostAverageRateDay = getLabourCostAverageRateDay();
            int iDayBufferForEngineeringProcess = getDayBufferForEngineeringProcess();
            int iWeekedBuffer = getWeekedBuffer(iLabourCostAverageRateDay+iDayBufferForEngineeringProcess);
            return iLabourCostAverageRateDay + iWeekedBuffer + iDayBufferForEngineeringProcess;
        }
        
        public int getWeekedBuffer(int iLabourCostTotalDay){
            
            int iWeekendBufferDay = 0;
            
            if(iLabourCostTotalDay<5){ iWeekendBufferDay = 0;
            }else if(iLabourCostTotalDay>=5 && iLabourCostTotalDay<12){ iWeekendBufferDay = 2;
            }else if(iLabourCostTotalDay>=12 && iLabourCostTotalDay<19){ iWeekendBufferDay = 4;
            }else if(iLabourCostTotalDay>=19 && iLabourCostTotalDay<26){ iWeekendBufferDay = 6;
            }else if(iLabourCostTotalDay>=26 && iLabourCostTotalDay<33){ iWeekendBufferDay = 8;
            }else if(iLabourCostTotalDay>=40 && iLabourCostTotalDay<47){ iWeekendBufferDay = 10;
            }else if(iLabourCostTotalDay>=47 && iLabourCostTotalDay<54){ iWeekendBufferDay = 12;
            }else if(iLabourCostTotalDay>=54 && iLabourCostTotalDay<61){ iWeekendBufferDay = 14;
            }else if(iLabourCostTotalDay>=61 && iLabourCostTotalDay<68){ iWeekendBufferDay = 16;
            }else if(iLabourCostTotalDay>=68 && iLabourCostTotalDay<75){ iWeekendBufferDay = 18;
            }else if(iLabourCostTotalDay>=75 && iLabourCostTotalDay<82){ iWeekendBufferDay = 20;
            }else if(iLabourCostTotalDay>=82 && iLabourCostTotalDay<89){ iWeekendBufferDay = 22;
            }else if(iLabourCostTotalDay>=89 && iLabourCostTotalDay<96){ iWeekendBufferDay = 24;
            }else if(iLabourCostTotalDay>=96 && iLabourCostTotalDay<103){ iWeekendBufferDay = 26;
            }else if(iLabourCostTotalDay>=103 && iLabourCostTotalDay<110){ iWeekendBufferDay = 28;
            }else if(iLabourCostTotalDay>=110 && iLabourCostTotalDay<117){ iWeekendBufferDay = 30;
            }else if(iLabourCostTotalDay>=117 && iLabourCostTotalDay<124){ iWeekendBufferDay = 32;
            }else if(iLabourCostTotalDay>=124 && iLabourCostTotalDay<131){ iWeekendBufferDay = 34;
            }else if(iLabourCostTotalDay>=131 && iLabourCostTotalDay<138){ iWeekendBufferDay = 36;
            }else if(iLabourCostTotalDay>=138 && iLabourCostTotalDay<145){ iWeekendBufferDay = 38;
            }else if(iLabourCostTotalDay>=145 && iLabourCostTotalDay<152){ iWeekendBufferDay = 40;
            }else if(iLabourCostTotalDay>=152 && iLabourCostTotalDay<159){ iWeekendBufferDay = 42;
            }else if(iLabourCostTotalDay>=159 && iLabourCostTotalDay<166){ iWeekendBufferDay = 44;
            }else if(iLabourCostTotalDay>=166 && iLabourCostTotalDay<173){ iWeekendBufferDay = 46;
            }else if(iLabourCostTotalDay>=173 && iLabourCostTotalDay<180){ iWeekendBufferDay = 48;
            }else if(iLabourCostTotalDay>=180 && iLabourCostTotalDay<187){ iWeekendBufferDay = 50;
            }else if(iLabourCostTotalDay>=187 && iLabourCostTotalDay<194){ iWeekendBufferDay = 52;
            }else if(iLabourCostTotalDay>=194 && iLabourCostTotalDay<201){ iWeekendBufferDay = 54;
            }else if(iLabourCostTotalDay>=201 && iLabourCostTotalDay<208){ iWeekendBufferDay = 56;
            }else if(iLabourCostTotalDay>=208 && iLabourCostTotalDay<215){ iWeekendBufferDay = 58;
            }else if(iLabourCostTotalDay>=215 && iLabourCostTotalDay<222){ 
                iWeekendBufferDay = 60;
            }
            
            return iWeekendBufferDay;
        }
        
        public int getLabourCostAverageRateDay(){
            
            
            BigDecimal bLabourCost = mathHelper.getNotNullDecimalValue(claim.getHireMonitoringDetail().getLabourCost());
            
            if(bLabourCost.compareTo(new BigDecimal(0.00))<1){
                bLabourCost = getNewLabourCost();
            }
            
            BigDecimal bAverageLabourHoursPerHireDay = new BigDecimal(claim.getChoBand().getAverageLabourHoursPerHireDay());
            BigDecimal bAverageLabourRate =  new BigDecimal(claim.getChoBand().getAverageLabourRate());
            BigDecimal bLabourCostAverageRateDay = new BigDecimal(0.00);
            
            if(bLabourCost.doubleValue()>0 
                    && bAverageLabourRate.doubleValue()>0 
                    && bAverageLabourHoursPerHireDay.doubleValue()>0){
                bLabourCostAverageRateDay = (bLabourCost.divide(bAverageLabourRate)).divide(bAverageLabourHoursPerHireDay);
            }

            return mathHelper.getIntegerFromDecimalRound(bLabourCostAverageRateDay);
            
        }
        
        public BigDecimal getNewLabourCost(){
            
            BigDecimal bLabourCost = new BigDecimal(0.00);
            BigDecimal bLabourRate = new BigDecimal(0.00);
            int iLabourHour = 0;
            
            if(claim.getHireMonitoringDetail()!=null){
                bLabourCost = mathHelper.getNotNullDecimalValue(claim.getHireMonitoringDetail().getLabourCost());
                bLabourRate = mathHelper.getNotNullDecimalValue(claim.getHireMonitoringDetail().getLabourRate());
                iLabourHour = mathHelper.getNotNullIntValue(claim.getHireMonitoringDetail().getLabourHour());
            }
            
            if((bLabourCost.compareTo(new BigDecimal(0.00))<1) && (iLabourHour>0)){
                
                if(bLabourRate.compareTo(new BigDecimal(0.00))<1){
                    bLabourCost = new BigDecimal(claim.getChoBand().getAverageLabourHoursPerHireDay()*iLabourHour);
                }else{
                    bLabourCost = bLabourRate.multiply(new BigDecimal(iLabourHour)); 
                }
            }
            return bLabourCost;
        }
        
        public int getDayBufferForEngineeringProcess(){
            
            int iDays = 0;
            
            if (claim.getHireMonitoringEcd()==null)
            {
                if (claim.getCustomerVehicleDamage().getIsUsable())
                {
                    iDays += claim.getChoBand().getTakeVehicleToGarageDaysMobile();
                }
                else
                {
                    iDays += claim.getChoBand().getTakeVehicleToGarageDaysNonMobile();
                }
            }
            else
            {
                if (claim.getCustomerVehicleDamage().getIsUsable())
                {
                    iDays += claim.getChoBand().getTakeVehicleToGarageDaysMobile();
                }
                else
                {
                    iDays += claim.getChoBand().getTakeVehicleToGarageDaysNonMobile();
                }
            }
            
            iDays += claim.getChoBand().getTakeVehicleOutDays();
            iDays += claim.getChoBand().getEngineerInspectionDelayDays();
                    
            return iDays;
        }
}
