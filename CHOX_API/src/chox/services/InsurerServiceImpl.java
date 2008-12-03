package chox.services;

import chox.Util.XmlHelper;
import chox.data.HibernateUtil;
import chox.model.Insurer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Element;

public class InsurerServiceImpl extends DataService implements InsurerService {
    
    public Insurer getInsurerByName(String s){
         
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
    
    public Insurer getObject(int id) {
       return (Insurer)currentSession.get(Insurer.class, id);
    }    
    
}
