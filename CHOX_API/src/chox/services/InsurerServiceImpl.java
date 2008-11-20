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
import org.w3c.dom.*;

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
        
        Session currentSession = HibernateUtil.currentSession();      
        Insurer insurer = new Insurer();
        
        try {
            
            Criteria criteria = currentSession.createCriteria(Insurer.class);
            criteria.add(Restrictions.eq("name", s));
            
            insurer = (Insurer) criteria.uniqueResult();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        currentSession.clear();
        currentSession.disconnect();
        
        
        return insurer;
}
    
    public static Insurer getInsurerByNodeName(Element thisElement, String nodeName) {
        Insurer insurer = new Insurer();
        
        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(thisElement, nodeName))){
            insurer = InsurerServiceImpl.getInsurerByName(XmlHelper.getNodeValue(thisElement, nodeName));
        }
        
        return insurer;
    }
    
}
