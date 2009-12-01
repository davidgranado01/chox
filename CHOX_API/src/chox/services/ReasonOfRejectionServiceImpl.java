package chox.services;

import chox.model.ReasonOfRejection;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

public class ReasonOfRejectionServiceImpl  extends SecureDataService implements ReasonOfRejectionService{

    public ReasonOfRejection getObject(int id) {
        return (ReasonOfRejection) get(ReasonOfRejection.class, id);
    }

    public void updateObject(ReasonOfRejection reasonOfRejection) {
        save(reasonOfRejection);
    }

     public List<ReasonOfRejection> getAllReasonOfRejection() {

        List<ReasonOfRejection> objects = new ArrayList<ReasonOfRejection>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class);
            criteria.addOrder(Order.asc("id"));
            objects = findByCriteria(criteria);

        } catch (Throwable e) {
           e.printStackTrace();
        }

        return objects;
    }
    
}
