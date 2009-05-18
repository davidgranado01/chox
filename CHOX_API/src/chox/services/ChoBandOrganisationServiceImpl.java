/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.ChoBandOrganisation;
import org.hibernate.criterion.Restrictions;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

public class ChoBandOrganisationServiceImpl extends SecureDataService implements ChoBandOrganisationService{

    public boolean isChoBandOccupied(int bandId){

        boolean isExist = false;

        List<ChoBandOrganisation> chobandorganisations = new ArrayList<ChoBandOrganisation>();
        
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(ChoBandOrganisation.class);
            criteria.add(Restrictions.eq("bandId", bandId));
            chobandorganisations = findByCriteria(criteria);

        } catch (Throwable e) {
           e.printStackTrace();
        }

        if(chobandorganisations.size()>0){
            isExist = true;
        }

        return isExist;

    }
    
    public boolean deleteChoBandOrganisationByBandId(int bandId){

        boolean bFlag = false;
        
        try{
            
            List<ChoBandOrganisation> chobandorganisations = new ArrayList<ChoBandOrganisation>();
            DetachedCriteria criteria = DetachedCriteria.forClass(ChoBandOrganisation.class);
            criteria.add(Restrictions.eq("bandId", bandId));
            chobandorganisations = findByCriteria(criteria);

            for(ChoBandOrganisation object : chobandorganisations){
                delete(object);
            }

            bFlag = true;
        } catch (Throwable e) {
           e.printStackTrace();
        }
        System.out.println("START DELETING BAND LIST PROCESS - 0001");
        return bFlag;
    }

    public ChoBandOrganisation getObject(int id) {
        return (ChoBandOrganisation) get(ChoBandOrganisation.class, id);
    }

    public void updateObject(ChoBandOrganisation object) {

        save(object);
    }

 }
