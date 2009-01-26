package chox.services;

import chox.Util.XmlHelper;
import chox.model.VehicleClass;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.*;

public class VehicleClassServiceImpl  extends DataService implements VehicleClassService{ 

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
            vehicleclass = getVehicleClassByName(XmlHelper.getNodeValue(thisElement, nodeName));
        }
        
        return vehicleclass;
    }

    public VehicleClass getObject(int id) {
       return (VehicleClass)get(VehicleClass.class, id);
    }
}
