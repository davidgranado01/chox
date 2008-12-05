package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.model.XMLParseResult;
import chox.model.Injury;
import chox.model.Solicitor;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import java.util.ArrayList;
import java.util.List;

public class SolicitorServiceImpl  extends DataService implements SolicitorService{

    public Solicitor getSolicitorByInjury(Injury injury){
         
        List solicitors = new ArrayList<Solicitor>();
        Solicitor solicitor = null;
        
        try {
            Criteria criteria = getCurrentSession().createCriteria(Solicitor.class).add(Restrictions.eq("injury", injury));
            solicitors = criteria.list();
            if(solicitors.size()>0){
                solicitor = (Solicitor)solicitors.get(0);
            }
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        
        
        
        return solicitor;
    }
    
    public XMLParseResult saveSolicitorForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getSolicitors())!=null){
            
            for(Integer i=0; i<(xmlParseResult.getSolicitors()).size(); i++){
                
                if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                    try{
                        xmlParseResult.getCurrentSession().saveOrUpdate(((xmlParseResult.getSolicitors()).get(i)));
                    } catch (Exception e) {
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                    }
                }
            }
        }
        
        return xmlParseResult;
    }  
    
        public Solicitor getObject(int id) {
        return (Solicitor) getCurrentSession().get(Solicitor.class, id);
    }

    public void updateObject(Solicitor solicitor) {

        getCurrentSession().beginTransaction();
        getCurrentSession().update(solicitor);
        getCurrentSession().getTransaction().commit();
    }
    
}
