package chox.services;

import chox.model.Injury;
import chox.model.Solicitor;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.DetachedCriteria;

public class SolicitorServiceImpl  extends SecureDataService implements SolicitorService{

    public Solicitor getSolicitorByInjury(Injury injury){

        Solicitor solicitor = null;
        
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Solicitor.class).add(Restrictions.eq("injury", injury));
            solicitor = (Solicitor)getByCriteria(criteria);
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return solicitor;
    }
    
    public void saveObjectForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getSolicitors()) != null) {

            for (Integer i = 0; i < (claimResult.getSolicitors()).size(); i++) {

                getHibernateTemplate().saveOrUpdate(((claimResult.getSolicitors()).get(i)));

            }
        }
    }
  
    public Solicitor getObject(int id) {
        return (Solicitor) get(Solicitor.class, id);
    }

    public void updateObject(Solicitor solicitor) {

        save(solicitor);
    }
    
}
