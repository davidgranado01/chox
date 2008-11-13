/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.data.HibernateUtil;
import chox.model.Claim;
import chox.model.Rental;
import chox.model.Supplier;
import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Emmanuel
 */
public class ClaimServiceImpl implements ClaimService {

    public List listAllClaims() {
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(Claim.class);
        return criteria.list();
    }

    public List listClaimsByStatus(String status) {
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(Claim.class)
                .add(Restrictions.eq("claimStatus", status));
        
        Supplier s = (Supplier)currentSession.load(Supplier.class, 999);
        Rental r = new Rental();
        r.setSupplierReference("ABC123q");
        r.setRentalStatus("Completed");
        r.setSupplier(s);
        
        try
        {
        currentSession.beginTransaction();
        currentSession.saveOrUpdate(r);
        currentSession.getTransaction().commit();
        }
        catch(Exception ex)
        {
            currentSession.getTransaction().rollback();
        }
        
        Rental r2 = (Rental)currentSession.load(Rental.class, r.getId());
        
    
        return criteria.list();
    }

    public ArrayList<XMLParseResult> processClaimXMLFile(File claimXMLFile, String sUpdateType, Boolean isAllowPartialUpload) {
        return XmlProcessController.XMLValidationProcess(claimXMLFile, sUpdateType, isAllowPartialUpload);
    }
}
