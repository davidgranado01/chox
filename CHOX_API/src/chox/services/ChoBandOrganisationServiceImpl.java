/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.ChoBandOrganisation;
import org.hibernate.criterion.Restrictions;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;

public class ChoBandOrganisationServiceImpl extends SecureDataService implements ChoBandOrganisationService{
    
    public boolean isActiveChorganisationWithBand(int choOrgid){
        
        boolean isExist = false;

        List<ChoBandOrganisation> chobandorganisations = new ArrayList<ChoBandOrganisation>();        
        
        try {

            chobandorganisations = getChoBandChorganisationsByChoOrgId(choOrgid);

        } catch (Throwable e) {
           e.printStackTrace();
        }        
        
        if(chobandorganisations.size()>0){
            isExist = true;
        }
        
        return isExist;
    }
    
    public boolean isChoBandOccupied(int bandId){

        boolean isExist = false;

        List<ChoBandOrganisation> chobandorganisations = new ArrayList<ChoBandOrganisation>();
        
        try {

            chobandorganisations = getChoBandChorganisationsByChoBandId(bandId);

        } catch (Throwable e) {
           e.printStackTrace();
        }

        if(chobandorganisations.size()>0){
            isExist = true;
        }
        return isExist;
    }
    
    public List<ChoBandOrganisation> getChoBandChorganisationsByChoOrgId(int choOrgid){
        
        List<ChoBandOrganisation> chobandorganisations = new ArrayList<ChoBandOrganisation>();
        
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(ChoBandOrganisation.class);
            criteria.add(Restrictions.eq("chorganisation.id", choOrgid));
            chobandorganisations = findByCriteria(criteria);

        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return chobandorganisations;
        
    }    
    
    public List<ChoBandOrganisation> getChoBandChorganisationsByChoBandId(int bandId){
        
        List<ChoBandOrganisation> chobandorganisations = new ArrayList<ChoBandOrganisation>();
        
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(ChoBandOrganisation.class);
            criteria.add(Restrictions.eq("choBand.id", bandId));
            chobandorganisations = findByCriteria(criteria);

        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return chobandorganisations;
        
    }
    
    public boolean deleteChoBandOrganisationByBandId(int bandId){

        boolean bFlag = false;
        
        try{
            
            List<ChoBandOrganisation> chobandorganisations = new ArrayList<ChoBandOrganisation>();
            DetachedCriteria criteria = DetachedCriteria.forClass(ChoBandOrganisation.class);
            criteria.add(Restrictions.eq("choBand.id", bandId));
            chobandorganisations = findByCriteria(criteria);

            for(ChoBandOrganisation object : chobandorganisations){
                delete(object);
            }

            bFlag = true;
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return bFlag;
    }

    public ChoBandOrganisation getObject(int id) {
        return (ChoBandOrganisation) get(ChoBandOrganisation.class, id);
    }

    public void updateObject(ChoBandOrganisation object) {

        save(object);
    }

 }
