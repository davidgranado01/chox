package chox.services;

import chox.model.Claim;
import chox.xmlValidation.rules.Util.XmlHelper;
import chox.model.Insurer;
import chox.model.VehicleClassCeiling;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Element;

public class InsurerServiceImpl extends SecureDataService implements InsurerService {
    
    public boolean isInsurerNameExist(String s){
        
        boolean isExist = false;
        
        if(getInsurerByName(s)!=null){
            isExist = true;
        }
        
        return isExist;
        
    }
    
    public Insurer getInsurerByName(String s) {

        Insurer insurer = new Insurer();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.add(Restrictions.eq("name", s));
            insurer = (Insurer) getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return insurer;
    }

    public Insurer getInsurerByNodeName(Element thisElement, String nodeName) {
        Insurer insurer = new Insurer();

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(thisElement, nodeName))) {
            insurer = getInsurerByName(XmlHelper.getNodeValue(thisElement, nodeName));
        }

        return insurer;
    }

    public Insurer getObject(int id) {
        return (Insurer) get(Insurer.class, id);
    }
    
    public List<Insurer> getInsurers(){
        
        List<Insurer> insurer = new ArrayList<Insurer>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.addOrder(Order.asc("name"));
            insurer = findByCriteria(criteria);
        
        } catch (Throwable e) {
           e.printStackTrace();
        }    
        
        return insurer;
    }    
    
    public Insurer updateObject(Insurer object) {
        
        try {
            save(object);
        } catch (Throwable e) {
           e.printStackTrace();
        }      
        
        return object;
    }  

    public VehicleClassCeiling getVechileClassCeilingForClaim(Claim claim)
    {
        VehicleClassCeiling vehicleClassCeiling = null;

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClassCeiling.class);
            criteria.add(Restrictions.eq("insurer", claim.getInsurer()));
            criteria.add(Restrictions.eq("vehicleClass", claim.getCustomer().getVehicleClass()));
            vehicleClassCeiling = (VehicleClassCeiling) getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        return vehicleClassCeiling;
    }
    
}
