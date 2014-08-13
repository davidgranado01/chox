package idas.chox.data.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Bordereau;
import idas.chox.core.model.BordereauWithoutFile;
import idas.chox.core.model.WebUser;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.BordereauService;

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

    @Override
    public SearchResult getUploadedFiles(WebUser webUser, int defaultDays, String sort, String dir, int start, int limit) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -defaultDays);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(BordereauWithoutFile.class);
        if (webUser.isAnInsurer()) {
            criteria.add(Restrictions.sqlRestriction("created_by in(select id from web_user where insurer_id =" + webUser.getInsurer().getId() + ")"));
        }
        else {
            criteria.add(Restrictions.sqlRestriction("created_by in(select id from web_user where chorganisation_id =" + webUser.getChorganisation().getId() + ")"));
        }
        if (defaultDays < 60) {
            try {
                criteria.add(Restrictions.ge("createdDate", dateFormat.parse(dateFormat.format(cal.getTime()))));
            } catch (ParseException ex) {
                LOG.debug("Parsing Exception thrown when getting today's date.");
            }
        }

        Integer totalCount = totalCount(criteria);

        if (sort != null && !sort.isEmpty() && dir != null && !dir.isEmpty()) {
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
        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);


        return new SearchResult(criteria.list(), totalCount, null);

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
