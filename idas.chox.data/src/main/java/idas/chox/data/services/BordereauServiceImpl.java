package idas.chox.data.services;

import idas.chox.core.model.Bordereau;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.BordereauService;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BordereauServiceImpl extends SecureDataService implements BordereauService {

    private static final Logger LOG = LoggerFactory.getLogger(BordereauServiceImpl.class);

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveBordereau(Bordereau bordereau) {
        save(bordereau);
    }

    @Override
    public Bordereau getBordereauByFileName(String fileName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Bordereau.class);
        criteria.add(Restrictions.eq("fileName", fileName));
        return (Bordereau) getByCriteria(criteria);
    }

    @Override
    public List<Bordereau> getBordereauByUserIdUploadedToday(WebUser webUser) {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DetachedCriteria criteria = DetachedCriteria.forClass(Bordereau.class);
        criteria.add(Restrictions.eq("createdBy", webUser));
        try {
            criteria.add(Restrictions.ge("createdDate", dateFormat.parse(dateFormat.format(cal.getTime()))));
        } catch (ParseException ex) {
            LOG.debug("Parsing Exception thrown when getting today's date.");
        }
        return findByCriteria(criteria);
    }

    @Override
    public Bordereau getBordereauById(int bordereauId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Bordereau.class);
        criteria.add(Restrictions.eq("id", bordereauId));
        return (Bordereau) getByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean deleteBordereau(Bordereau bordereau){
           try{
               delete(bordereau);
               return true;
           }catch(Exception ex){
               LOG.error("UPLOADED AND UNPROCESSED CAN NOT BE DELETED REASON : {}",ex.getMessage());
               return false;
           }

    }


   
}
