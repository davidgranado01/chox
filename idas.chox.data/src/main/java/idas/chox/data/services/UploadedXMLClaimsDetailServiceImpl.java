package idas.chox.data.services;

import idas.chox.core.model.UploadedXMLClaimsDetail;
import idas.chox.core.services.UploadedXMLClaimsDetailService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author seeni
 */
public class UploadedXMLClaimsDetailServiceImpl extends SecureDataService implements UploadedXMLClaimsDetailService {

    @Override
    public List<UploadedXMLClaimsDetail> getUploadedXMLClaimsDetailByBordereauId(int bordereauId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UploadedXMLClaimsDetail.class);
        criteria.add(Restrictions.eq("bordereauId", bordereauId));
        return findByCriteria(criteria);
    }

    @Override
    public void saveUploadedXMLClaimsDetail(UploadedXMLClaimsDetail claimsDetail) {
        save(claimsDetail);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveUploadedXMLClaimsDetails(List<? extends Object> objects) {
        saveCollections(objects);
    }
}
