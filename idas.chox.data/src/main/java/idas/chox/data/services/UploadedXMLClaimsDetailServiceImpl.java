package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.UploadedXMLClaimsDetail;
import idas.chox.core.services.UploadedXMLClaimsDetailService;

/**
 *
 * @author seeni
 */
public class UploadedXMLClaimsDetailServiceImpl extends SecureDataService implements UploadedXMLClaimsDetailService {

    @Override
    @Transactional(readOnly = true, value="transactionManager")
    public List<UploadedXMLClaimsDetail> getUploadedXMLClaimsDetailByBordereauId(int bordereauId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UploadedXMLClaimsDetail.class);
        criteria.add(Restrictions.eq("bordereauId", bordereauId));
        return findByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void saveUploadedXMLClaimsDetail(UploadedXMLClaimsDetail claimsDetail) {
        save(claimsDetail);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void saveUploadedXMLClaimsDetails(List<UploadedXMLClaimsDetail> objects) {
        saveCollections(objects);
    }
}
