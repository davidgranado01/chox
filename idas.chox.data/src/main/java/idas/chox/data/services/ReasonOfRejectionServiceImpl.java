package idas.chox.data.services;

import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.services.ReasonOfRejectionService;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

public class ReasonOfRejectionServiceImpl extends SecureDataService implements ReasonOfRejectionService {

    public ReasonOfRejection getReasonOfRejection(int id) {
        return (ReasonOfRejection) get(ReasonOfRejection.class, id);
    }

    public List<ReasonOfRejection> getAllReasonOfRejection() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfRejection.class);
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }
}
