/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.ChoBand;
import org.hibernate.criterion.Restrictions;
import java.math.BigDecimal;
import org.hibernate.criterion.DetachedCriteria;

public class ChoBandServiceImpl extends SecureDataService implements ChoBandService{
    
    public ChoBand getDummyChoBand()
    {
        ChoBand choband = new ChoBand();
        choband.setEngineerInspectionDelayDays(2);
        choband.setHireDayCeiling(22);
        choband.setHireNetCeiling(new BigDecimal("1500.00"));
        choband.setHireRateChargeTolerance(new BigDecimal("0.01"));
        choband.setInspectionDelayDays(4);
        choband.setIsMobileDayAllowance(2);
        choband.setIsNotMobileDayAllowance(9);
        choband.setMaxRepairValue(new BigDecimal("1500.00"));
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
            
            DetachedCriteria criteria = DetachedCriteria.forClass(ChoBand.class);
            criteria.add(Restrictions.eq("Id", orgId));
            band = (ChoBand) getByCriteria(criteria);
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        
        
        return band;
    }
    
    public ChoBand getChoBandByChorganisationIdAndInsurerId(int orgId, int insurerId){
        
        ChoBand band = new ChoBand();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(ChoBand.class);
            criteria.add(Restrictions.eq("Id", orgId));
            band = (ChoBand)getByCriteria(criteria);
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        
        
        return band;
    }    
}
