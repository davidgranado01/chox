/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.ChoBand;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import java.util.Date;
import java.math.BigDecimal;

public class ChoBandServiceImpl extends DataService implements ChoBandService{
    
    public ChoBand getDummyChoBand()
    {
        ChoBand choband = new ChoBand();
        choband.setEngineerInspectionDelayDays(2);
        choband.setHireDayCeiling(22);
        choband.setHireNetCeiling(new BigDecimal("1000.00"));
        choband.setHireRateChargeTolerance(new BigDecimal("0.01"));
        choband.setInspectionDelayDays(4);
        choband.setIsMobileDayAllowance(2);
        choband.setIsNotMobileDayAllowance(9);
        choband.setMaxRepairValue(new BigDecimal("1000.00"));
        choband.setOfferMadeDays(7);
        choband.setReceiptOfFinalStatementChequeDays(10);
        choband.setTakeVehicleOutDays(1);
        choband.setTakeVehicleToGarageDaysMobile(1);
        choband.setTakeVehicleToGarageDaysNonMobile(3);
        choband.setWeekendBufferDays(2);
        return choband;
    }

    public ChoBand getChoBandByChorganisationId(int orgId){
        
        ChoBand band = new ChoBand();
        
        try {
            
            Criteria criteria = getCurrentSession().createCriteria(ChoBand.class);
            criteria.add(Restrictions.eq("Id", orgId));
            band = (ChoBand) criteria.uniqueResult();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        
        
        return band;
    }
    
    public ChoBand getChoBandByChorganisationIdAndInsurerId(int orgId, int insurerId){
        
        ChoBand band = new ChoBand();
        
        try {
            
            Criteria criteria = getCurrentSession().createCriteria(ChoBand.class);
            criteria.add(Restrictions.eq("Id", orgId));
            band = (ChoBand) criteria.uniqueResult();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        
        
        return band;
    }    
}
