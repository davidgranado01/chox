package chox.services;

import chox.Util.TextHelper;
import chox.xmlValidation.rules.Util.XmlHelper;
import chox.model.VehicleClass;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.*;

public class VehicleClassServiceImpl  extends SecureDataService implements VehicleClassService{ 

    public VehicleClass getVehicleClassByName(String s){
  
        VehicleClass vehicleclass = null;
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClass.class);
            criteria.add(Restrictions.eq("name", s));
            
            vehicleclass = (VehicleClass) getByCriteria(criteria);
            
        } catch (Throwable e) {
           e.printStackTrace();
        }       
  
        return vehicleclass;
    }
    
    public VehicleClass getVehicleClassByNodeName(Element thisElement, String nodeName) {
        
        VehicleClass vehicleclass = null;
        
        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(thisElement, nodeName))){
            String vehicleName = TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(thisElement, nodeName));
            vehicleclass = getVehicleClassByName(vehicleName.toUpperCase());
        }
        
        return vehicleclass;
    }

    public VehicleClass getObject(int id) {
       return (VehicleClass)get(VehicleClass.class, id);
    }

    public List getAllVehicleClass()
    {
        DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClass.class);
        criteria.addOrder(Order.asc("name"));
        return this.findByCriteria(criteria);
    }
}
