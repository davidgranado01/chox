package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.KeoghsRequest;
import idas.chox.core.services.KeoghsRequestService;
import java.util.ArrayList;
import java.util.HashMap;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.transform.Transformers;

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
    public List<KeoghsRequest> getKeoghsRequestByClaimId(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(KeoghsRequest.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.addOrder(Order.asc("createdDate"));

        return findByCriteria(criteria);
    }

    @Override
    public List<KeoghsRequest> getKeoghsRequestByClaim(Claim claim) {
        DetachedCriteria criteria = DetachedCriteria.forClass(KeoghsRequest.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));
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
    public List<KeoghsRequest> getQueuedRequests(int maxRequests) {
        // An open request will have a batch status of null (for new requests) or >=0 and < 5,
        // and a claim status code of  >=0 and < 9
        // NB. A batch status of 5 and a claim status of 9 equates to a successful call
        // NB. A claim status of 0 is set when a claim is submitted
//        DetachedCriteria criteria = DetachedCriteria.forClass(KeoghsRequest.class).add(Restrictions.disjunction().add(Restrictions.isNull("batchStatus")).add(Restrictions.between("batchStatus", new Integer("0"), new Integer("4"))));
        // Maybe be better changing this to restrict on the claim.fraudCheckStatus of 1 (queued)
//        DetachedCriteria criteria = DetachedCriteria.forClass(KeoghsRequest.class).add(Restrictions.eq("batchStatus", 0));
//        criteria.createCriteria("claim").add(Restrictions.eq("fraudCheckStatus", 1));
        Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(KeoghsRequest.class)
                .add(Restrictions.isNull("resultStatus"))
                .createAlias("this.claim", "c", CriteriaSpecification.LEFT_JOIN);
        criteria.add(Restrictions.eq("c.fraudCheckStatus", 1));
        criteria.addOrder(Order.desc("createdDate"));
        if (maxRequests > 0) {
            criteria.setMaxResults(maxRequests);
        }

        criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<HashMap> resultMap = criteria.list();

        List<KeoghsRequest> keoghsRequests = new ArrayList<>();

        for (HashMap m : resultMap) {
            keoghsRequests.add((KeoghsRequest) m.get("this"));
        }
        
//        return findByCriteria(criteria);
        return keoghsRequests;
    }
    
    @Override
    public List<KeoghsRequest> getPendingRequests() {
        // An open request will have a batch status of null (for new requests) or >=0 and < 5,
        // and a claim status code of  >=0 and < 9
        // NB. A batch status of 5 and a claim status of 9 equates to a successful call
        // Queued requests will have a claim.fraud_check_status of 1
        // Pending requests will have a claim.fraud_check_status of 2
        DetachedCriteria criteria = DetachedCriteria.forClass(KeoghsRequest.class);
        criteria.createCriteria("claim").add(Restrictions.eq("fraudCheckStatus", 2));
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }
    
}
