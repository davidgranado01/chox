package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.KeoghsRequest;
import idas.chox.core.services.KeoghsRequestService;

/**
 *
 * @author John
 */
public class KeoghsRequestServiceImpl extends SecureDataService implements KeoghsRequestService {

    @Override
    public KeoghsRequest getKeoghsRequest(int id) {
        return (KeoghsRequest) get(KeoghsRequest.class, id);
    }

    @Override
    public void saveKeoghsRequest(KeoghsRequest keoghsRequest) {
        save(keoghsRequest);
    }

    @Override
    public List<KeoghsRequest> getKeoghsRequestByClaim(Claim claim) {
        DetachedCriteria criteria = DetachedCriteria.forClass(KeoghsRequest.class).add(Restrictions.eq("claim", claim));
        criteria.addOrder(Order.asc("createdDate"));

        return findByCriteria(criteria);
    }

    @Override
    public KeoghsRequest getKeoghsRequestByClientBatchReference(String clientBatchReference) {
        DetachedCriteria criteria = DetachedCriteria.forClass(KeoghsRequest.class).add(Restrictions.eq("clientBatchReference", clientBatchReference));
        KeoghsRequest keoghsRequest = (KeoghsRequest) getByCriteria(criteria);

        return keoghsRequest;
    }

    @Override
    public List<KeoghsRequest> getQueuedRequests() {
        // An open request will have a batch status of null (for new requests) or >=0 and < 5,
        // and a claim status code of  >=0 and < 9
        // NB. A batch status of 5 and a claim status of 9 equates to a successful call
        // NB. A claim status of 0 is set when a claim is submitted
//        DetachedCriteria criteria = DetachedCriteria.forClass(KeoghsRequest.class).add(Restrictions.disjunction().add(Restrictions.isNull("batchStatus")).add(Restrictions.between("batchStatus", new Integer("0"), new Integer("4"))));
        // Maybe be better changing this to restrict on the claim.fraudCheckStatus of 1 (queued)
        DetachedCriteria criteria = DetachedCriteria.forClass(KeoghsRequest.class).add(Restrictions.eq("batchStatus", 0));
        criteria.createCriteria("claim").add(Restrictions.eq("fraudCheckStatus", 1));
        criteria.addOrder(Order.desc("createdDate"));
        return findByCriteria(criteria);
    }
    
    @Override
    public List<KeoghsRequest> getPendingRequests() {
        // An open request will have a batch status of null (for new requests) or >=0 and < 5,
        // and a claim status code of  >=0 and < 9
        // NB. A batch status of 5 and a claim status of 9 equates to a successful call
        // Queued request will have a claim.fraud_check_status of 1
        
        // Would be better changing this to restrict on the claim.fraudCheckStatus of 2 (pending)
        DetachedCriteria criteria = DetachedCriteria.forClass(KeoghsRequest.class).add(Restrictions.ne("batchStatus", 5));
        criteria.createCriteria("claim").add(Restrictions.eq("fraudCheckStatus", 2));
        criteria.addOrder(Order.desc("createdDate"));
        return findByCriteria(criteria);
    }
    
}
