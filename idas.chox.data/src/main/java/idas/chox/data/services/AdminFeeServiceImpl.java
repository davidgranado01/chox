package idas.chox.data.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.AdminFee;
import idas.chox.core.services.AdminFeeService;


/**
 *
 * @author John
 */
public class AdminFeeServiceImpl extends SecureDataService implements AdminFeeService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminFeeServiceImpl.class);

    @Override
    public BigDecimal getAdminFee(Date startDate, boolean coverNoteRequired, boolean managingRepair) throws Exception {
        DetachedCriteria criteria = DetachedCriteria.forClass(AdminFee.class);
        criteria.add(Restrictions.le("startDate", startDate));
        criteria.add(Restrictions.eq("coverNoteRequired", coverNoteRequired));
        criteria.add(Restrictions.eq("managingRepair", managingRepair));
        criteria.addOrder(Order.desc("startDate"));

        List<AdminFee> adminFees = null;
        try {
            adminFees = this.findByCriteria(criteria);
        } catch (Exception ex) {
            LOG.error("Exception caught getting admin fees: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
        }
        if (adminFees == null || adminFees.isEmpty()) {
            LOG.warn("No admin fee found for start date '{}' and coverNoteRequired={}, managingRepair={}",
                    new Object[]{startDate, coverNoteRequired, managingRepair});
            throw new Exception("No admin fee found.");
        }

        LOG.debug("Found {} rows: Returning fee={} for startDate={}, coverNoteRequired={}, managingRepair={}",
                new Object[]{adminFees.size(), ((AdminFee) adminFees.get(0)).getFee(), startDate, coverNoteRequired, managingRepair});

        return ((AdminFee) adminFees.get(0)).getFee();
    }
}
