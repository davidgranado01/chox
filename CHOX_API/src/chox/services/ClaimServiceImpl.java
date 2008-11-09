/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.data.HibernateUtil;
import chox.model.Claim;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 *
 * @author Emmanuel
 */
public class ClaimServiceImpl implements ClaimService {
    
    public List getAllClaims()
    {
       Session currentSession = HibernateUtil.currentSession();
       Criteria criteria = currentSession.createCriteria(Claim.class);       
       return criteria.list();
    }   
    
    public void InsertDummyClaims()
    {
        Claim c4 = new Claim();
        c4.setTpClaimReference("C4");
        c4.setRentalId(1000000);
        c4.setPolicyHolderName("AhKeong");
        c4.setVehicleRegistration("C48793FG");
        c4.setVehicleManufacturer("Hinda");
        c4.setVehicleModel("Civil 1.8 RX");
        c4.setVehicleClassId(1000000);
        c4.setUsable("y");
        c4.setInsurerCountryId(1000);
        c4.setIncidentDate(java.util.Calendar.getInstance().getTime());
        c4.setPoliceInvolved("y");
        c4.setClaimStatus("Awaiting Authorization");       
        c4.setTpInsurerCountryId(1000);
        c4.setTpVehicleClassId(1000000);
        c4.setProposedRentalClassId(1000000);        
        
        Session currentSession = HibernateUtil.currentSession();
        currentSession.beginTransaction();
        currentSession.save(c4);
        currentSession.getTransaction().commit();
        
        
    }

    
}
