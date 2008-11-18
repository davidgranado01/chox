package chox.services;

import chox.data.HibernateUtil;
import chox.model.VehicleClass;
import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class VehicleClassServiceImpl {

    public static VehicleClass  getVehicleClassByName(String s){
        VehicleClass vehicleclass = new VehicleClass();
        
        /*
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(VehicleClass.class).add(Restrictions.eq("name", s));
        
        if(criteria.list().size()>0){
            //insurerId = "";
        }
        */
        
        return vehicleclass;
    }
}
