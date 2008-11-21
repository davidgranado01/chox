package chox.services;

import chox.data.HibernateUtil;
import chox.model.Insurer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Element;

public class InsurerServiceImpl implements InsurerService {
    
    /*
    public Boolean isInsurerExistByName(String s){
        Boolean isExist = false;
        
        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(Insurer.class).add(Restrictions.eq("name", s));
        
        if(criteria.list().size()>0){
            isExist = true;
        }
        
        return isExist;
    }
    */
    
    public Insurer getInsurerByName(String s){
        
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
    
    public Insurer getInsurerByNodeName(Element thisElement, String nodeName) {
        Insurer insurer = new Insurer();
        
        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(thisElement, nodeName))){
            insurer = getInsurerByName(XmlHelper.getNodeValue(thisElement, nodeName));
        }
        
        return insurer;
    }
    
}
