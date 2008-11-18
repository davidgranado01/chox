package chox.services;

import chox.data.HibernateUtil;
import chox.model.Insurer;
import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class InsurerServiceImpl{

    public static Boolean isInsurerExistByName(String s){
        Boolean isExist = false;
        
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(Insurer.class).add(Restrictions.eq("name", s));
        
        if(criteria.list().size()>0){
            isExist = true;
        }
        
        return isExist;
    }
    
    public static Insurer getInsurerByName(String s){
        
        Insurer insurer = new Insurer();
        
        // Session currentSession = HibernateUtil.currentSession();
        // Criteria criteria = currentSession.createCriteria(Insurer.class).add(Restrictions.eq("name", s));
        
        // if(criteria.list().size()>0){
            //insurerId = "";
        //}
        insurer.setId(1);
        return insurer;
    }
    
}
