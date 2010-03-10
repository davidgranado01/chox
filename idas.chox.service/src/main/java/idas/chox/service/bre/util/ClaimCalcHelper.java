package idas.chox.service.bre.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import java.math.BigDecimal;
import java.util.Date;

public class ClaimCalcHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimCalcHelper.class);

	private Claim claim;
        private ExtrasCalcHelper exCalcHelper;
	
	private ClaimCalcHelper(){ }
	
	public static ClaimCalcHelper getInstance(Claim c)
	{
            ClaimCalcHelper cc = new ClaimCalcHelper();
            cc.claim = c;
            cc.exCalcHelper = ExtrasCalcHelper.getInstance(c.getInvoice());
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
            Date hireStart = claim.getVehicleHire().getRentalStart();
            Date initialEcd = claim.getLatestHireMonitoringEcdDate();
            int hireDuration = CalcHelper.getDaysBetweenDates(hireStart, initialEcd);
            LOG.debug("Hire duration period from {} to {}", hireStart, initialEcd);
            LOG.debug("Hire duration is {}", hireDuration);
            return hireDuration;
	}
        
	public BigDecimal getDailyHireRateCharged()
	{
            BigDecimal hireNetMinusExtras = claim.getInvoice().getHireNet().subtract(exCalcHelper.getTotalExtras());
            return hireNetMinusExtras.divide(new BigDecimal(claim.getVehicleHire().getDays()), 4, 1);
	}
        
    /*
	public BigDecimal getDailyHireRateChargedWithToleranceDeduction()
	{
            BigDecimal tolerance = getDailyHireRateCharged().multiply(claim.getBreBand().getHireRateChargeTolerance());            
            return getDailyHireRateCharged().subtract(tolerance);
	}
    */

	public int getAllowedDays()
	{
		int allowedDays = 0;

                // Basecamp: S8019
		// allowedDays += claim.getBreBand().getWeekendBufferDays();
                allowedDays += getWeekendBuffer();
                LOG.debug("Adding to allowable days: TakeVehicleOutDays={}",claim.getBreBand().getTakeVehicleOutDays());
		allowedDays += claim.getBreBand().getTakeVehicleOutDays();
                LOG.debug("Adding to allowable days: EngineerInspectionDelayDays={}",claim.getBreBand().getEngineerInspectionDelayDays());
		allowedDays += claim.getBreBand().getEngineerInspectionDelayDays();

		//if (claim.getCustomerVehicleDamage().getInitialECD() == null) //no ecd
                if (claim.getLatestHireMonitoringEcdDate()==null) //no ecd
		{
                    if (claim.getCustomer().getIsUsable())
                    {
                            LOG.debug("Adding to allowable days: TakeVehicleToGarageDaysMobile={}",claim.getBreBand().getTakeVehicleToGarageDaysMobile());
                            allowedDays += claim.getBreBand().getTakeVehicleToGarageDaysMobile();
                            LOG.debug("Adding to allowable days: IsMobileDayAllowance={}",claim.getBreBand().getIsMobileDayAllowance());
                            allowedDays += claim.getBreBand().getIsMobileDayAllowance();
                    }
                    else
                    {
                            LOG.debug("Adding to allowable days: TakeVehicleToGarageDaysNonMobile={}", claim.getBreBand().getTakeVehicleToGarageDaysNonMobile());
                            allowedDays += claim.getBreBand().getTakeVehicleToGarageDaysNonMobile();
                            LOG.debug("Adding to allowable days: IsNotMobileDayAllowance={}", claim.getBreBand().getIsNotMobileDayAllowance());
                            allowedDays += claim.getBreBand().getIsNotMobileDayAllowance();
                    }
		}
		else //we have an ecd
		{
                    allowedDays += getHireDuration() + 1;

                    if (claim.getCustomer().getIsUsable())
                    {
                        LOG.debug("Adding to allowable days: TakeVehicleToGarageDaysMobile={}", claim.getBreBand().getTakeVehicleToGarageDaysMobile());
                        allowedDays += claim.getBreBand().getTakeVehicleToGarageDaysMobile();
                    }
                    else
                    {
                        LOG.debug("Adding to allowable days: TakeVehicleToGarageDaysNonMobile={}", claim.getBreBand().getTakeVehicleToGarageDaysNonMobile());
                        allowedDays += claim.getBreBand().getTakeVehicleToGarageDaysNonMobile();
                    }
		}
                
		return allowedDays;
	}   
        
        public int getNumberDayOfLabourCostWorthy(){
            
            int iLabourCostAverageRateDay = getLabourCostAverageRateDay();
            int iDayBufferForEngineeringProcess = getDayBufferForEngineeringProcess();
            int iWeekedBuffer = getWeekendBuffer();
            
            // System.out.println("iLabourCostAverageRateDay:"+iLabourCostAverageRateDay);
            // System.out.println("iDayBufferForEngineeringProcess:"+iDayBufferForEngineeringProcess);
            // System.out.println("iWeekedBuffer:"+iWeekedBuffer);
            
            return iLabourCostAverageRateDay + iWeekedBuffer + iDayBufferForEngineeringProcess;
        }

        public int getWeekendBuffer(){

            int iWeekendBufferDay = 0;

            int iLabourCostAverageRateDay = getLabourCostAverageRateDay();
            int iDayBufferForEngineeringProcess = getDayBufferForEngineeringProcess();
            
            int iLabourCostTotalDay = iLabourCostAverageRateDay + iDayBufferForEngineeringProcess;
                    
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

            LOG.debug("Weekend buffer is {}", iWeekendBufferDay);
            return iWeekendBufferDay;
        }
        
        public int getLabourCostAverageRateDay(){
            
            BigDecimal bLabourCost = mathHelper.getNotNullDecimalValue(claim.getHireMonitoringDetail().getLabourCost());
            
            if(bLabourCost.compareTo(new BigDecimal(0.00))<1){
                bLabourCost = getNewLabourCost();
            }
            
            BigDecimal bAverageLabourHoursPerHireDay = new BigDecimal(claim.getBreBand().getAverageLabourHoursPerHireDay());
            BigDecimal bAverageLabourRate =  new BigDecimal(claim.getBreBand().getAverageLabourRate());
            
            BigDecimal bLabourCostAverageRateDay = new BigDecimal(0.00);
            
            if(bLabourCost.doubleValue()>0 
                    && bAverageLabourRate.doubleValue()>0 
                    && bAverageLabourHoursPerHireDay.doubleValue()>0){
                
                bLabourCostAverageRateDay = (bLabourCost.divide(bAverageLabourRate)).divide(bAverageLabourHoursPerHireDay);
            }

            return mathHelper.getIntegerFromDecimalRoundUp(bLabourCostAverageRateDay);
            
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
                    bLabourCost = new BigDecimal(claim.getBreBand().getAverageLabourRate()*iLabourHour);
                }else{
                    bLabourCost = bLabourRate.multiply(new BigDecimal(iLabourHour)); 
                }
                
            }
            
            return bLabourCost;
        }
        
        public int getDayBufferForEngineeringProcess(){
            
            int iDays = 0;
            
            if (claim.getLatestHireMonitoringEcdDate()==null)
            {
                if (claim.getCustomer().getIsUsable())
                {
                    iDays += claim.getBreBand().getTakeVehicleToGarageDaysMobile();
                }
                else
                {
                    iDays += claim.getBreBand().getTakeVehicleToGarageDaysNonMobile();
                }
            }
            else
            {
                if (claim.getCustomer().getIsUsable())
                {
                    iDays += claim.getBreBand().getTakeVehicleToGarageDaysMobile();
                }
                else
                {
                    iDays += claim.getBreBand().getTakeVehicleToGarageDaysNonMobile();
                }
            }
            
            iDays += claim.getBreBand().getTakeVehicleOutDays();
            iDays += claim.getBreBand().getEngineerInspectionDelayDays();
                    
            return iDays;
        }
}
