package chox.services;

import chox.model.VehicleClass;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.*;

public class VehicleClassServiceImpl  extends DataService implements VehicleClassService{ 

    public VehicleClass getVehicleClassByName(String s){
  
        VehicleClass vehicleclass = new VehicleClass();
        
        try {
            
            Criteria criteria = currentSession.createCriteria(VehicleClass.class);
            criteria.add(Restrictions.eq("name", s));
            
            vehicleclass = (VehicleClass) criteria.uniqueResult();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }       
  
        return vehicleclass;

    }
    
    public VehicleClass getVehicleClassByNodeName(Element thisElement, String nodeName) {
        
        VehicleClass vehicleclass = new VehicleClass();
        
        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(thisElement, nodeName))){
            vehicleclass = getVehicleClassByName(XmlHelper.getNodeValue(thisElement, nodeName));
        }
        
        return vehicleclass;
    }
}
