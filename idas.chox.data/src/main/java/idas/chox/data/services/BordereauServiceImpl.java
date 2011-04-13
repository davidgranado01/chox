package idas.chox.data.services;

import idas.chox.core.model.Bordereau;
import idas.chox.core.model.BordereauWithoutFile;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.BordereauService;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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
    public Bordereau getBordereauById(int bordereauId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Bordereau.class);
        criteria.add(Restrictions.eq("id", bordereauId));
        return (Bordereau) getByCriteria(criteria);
    }

    private void addSort(DetachedCriteria criteria, String sort, String dir) {
        if (dir.equalsIgnoreCase("desc")) {
            criteria.addOrder(Order.desc(sort));
        } else {
            criteria.addOrder(Order.asc(sort));
        }
    }

    @Override
    public List<BordereauWithoutFile> getUploadedFiles(WebUser webUser, int defaultDays, String sort, String dir) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -defaultDays);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DetachedCriteria criteria = DetachedCriteria.forClass(BordereauWithoutFile.class);
        criteria.add(Restrictions.eq("createdBy", webUser));
        if (defaultDays < 60) {
            try {
                criteria.add(Restrictions.ge("createdDate", dateFormat.parse(dateFormat.format(cal.getTime()))));
            } catch (ParseException ex) {
                LOG.debug("Parsing Exception thrown when getting today's date.");
            }
        }
        if (!sort.isEmpty() && !dir.isEmpty()) {
            if (sort.equalsIgnoreCase("fileName")) {
                addSort(criteria, "fileName", dir);
            } else if (sort.equalsIgnoreCase("fileSize")) {
                addSort(criteria, "fileSize", dir);
            } else if (sort.equalsIgnoreCase("processed")) {
                addSort(criteria, "processed", dir);
            } else if (sort.equalsIgnoreCase("message")) {
                addSort(criteria, "message", dir);
            } else if (sort.equalsIgnoreCase("createdBy")) {
                addSort(criteria, "createdBy", dir);
            } else if (sort.equalsIgnoreCase("createdDate")) {
                addSort(criteria, "createdDate", dir);
            } else if (sort.equalsIgnoreCase("status")) {
                addSort(criteria, "status", dir);
            } else if (sort.equalsIgnoreCase("totalClaims")) {
                addSort(criteria, "totalClaims", dir);
            } else if (sort.equalsIgnoreCase("valid")) {
                addSort(criteria, "valid", dir);
            } else if (sort.equalsIgnoreCase("description")) {
                addSort(criteria, "description", dir);
            }
        }
        return findByCriteria(criteria);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean deleteBordereau(Bordereau bordereau) {
        try {
            delete(bordereau);
            return true;
        } catch (Exception ex) {
            LOG.error("UPLOADED AND UNPROCESSED CAN NOT BE DELETED REASON : {}", ex.getMessage());
            return false;
        }

    }
}
