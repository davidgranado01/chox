package chox.services;

import chox.data.HibernateUtil;
import chox.model.VehicleClass;
import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.*;
import org.hibernate.Session;
import java.util.Iterator;

public class VehicleClassServiceImpl {

    public static VehicleClass  getVehicleClassByName(String s){

        Session currentSession = HibernateUtil.currentSession();      
        VehicleClass vehicleclass = new VehicleClass();
        
        try {
            
            Criteria criteria = currentSession.createCriteria(VehicleClass.class);
            criteria.add(Restrictions.eq("name", s));
            
            vehicleclass = (VehicleClass) criteria.uniqueResult();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        currentSession.clear();
        currentSession.disconnect();
        
        
        return vehicleclass;

    }
    
    public static VehicleClass getVehicleClassByNodeName(Element thisElement, String nodeName) {
        
        VehicleClass vehicleclass = new VehicleClass();
        
        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(thisElement, nodeName))){
            vehicleclass = VehicleClassServiceImpl.getVehicleClassByName(XmlHelper.getNodeValue(thisElement, nodeName));
        }
        
        return vehicleclass;
    }
}
