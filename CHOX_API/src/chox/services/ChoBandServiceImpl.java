/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.ChoBand;
import chox.model.ChoBandOrganisation;
import org.hibernate.criterion.Restrictions;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;


public class ChoBandServiceImpl extends SecureDataService implements ChoBandService{

    private ChoBandOrganisationService chobandorganisaionservice;

    public void setChoBandOrganisationService(ChoBandOrganisationService chobandorganisaionservice)
    {
        this.chobandorganisaionservice = chobandorganisaionservice;
    }

    
    public List<ChoBand> getInsurerChoBand(int insurerId){

        List<ChoBand> choBand = new ArrayList<ChoBand>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(ChoBand.class);

            if(insurerId>0){
                
                criteria.add(Restrictions.eq("insurer.id", insurerId));
                
            }
            
            criteria.addOrder(Order.asc("name"));
            choBand = findByCriteria(criteria);

        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return choBand;
    }

    public List<ChoBand> getInsurerChoBand(){

        List<ChoBand> choBand = new ArrayList<ChoBand>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(ChoBand.class);
            criteria.addOrder(Order.asc("name"));
            choBand = findByCriteria(criteria);

        } catch (Throwable e) {
           e.printStackTrace();
        }

        return choBand;
    }

    public boolean isChoBandNameExist(ChoBand object){

        boolean bFlag = false;
        
        List<ChoBand> choBand = new ArrayList<ChoBand>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(ChoBand.class);
            criteria.add(Restrictions.eq("name", object.getName()));
            criteria.add(Restrictions.eq("insurer.id", object.getInsurer().getId()));
            
            if(object.getId()>0){
               criteria.add(Restrictions.ne("id", object.getId())); 
            }
            
            choBand = findByCriteria(criteria);

        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        if(choBand.size()>0){
            bFlag = true;
        }
        
        return bFlag;
    }
    
    public boolean isChoBandOccupied(ChoBand object){
        return chobandorganisaionservice.isChoBandOccupied(object.getId());
    }

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
        choband.setAverageLabourHoursPerHireDay(4);
        choband.setAverageLabourRate(40);
        return choband;
    }

    public ChoBand getObject(int id) {
        return (ChoBand) get(ChoBand.class, id);
    }

    public void updateObject(ChoBand object) {

        save(object);
    }

    public boolean deleteObject(ChoBand object){
        
        boolean bFlag = false;

        try{

            if(chobandorganisaionservice.deleteChoBandOrganisationByBandId(object.getId())){

                delete(object);
                bFlag = true;
            }
            
        } catch (Throwable e) {
            bFlag = false;
        }

        return bFlag;
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
            
            List<ChoBandOrganisation> bandChorgs = chobandorganisaionservice.getChoBandChorganisationsByChoOrgId(orgId);
            
            for(ChoBandOrganisation object : bandChorgs){
                if(object.getChoBand().getInsurer().getId()==insurerId){
                    band = object.getChoBand();
                    break;
                }
            }
            
            /*
            DetachedCriteria criteria = DetachedCriteria.forClass(ChoBand.class);
            criteria.add(Restrictions.eq("Id", orgId));
            band = (ChoBand)getByCriteria(criteria);
            */
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return band;
    }    
}
