package idas.chox.data.services;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UserWorkgroupService;
import idas.chox.core.services.WorkgroupService;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

public class WorkgroupServiceImpl extends SecureDataService implements WorkgroupService {

    protected UserWorkgroupService userWorkgroupService;
    protected ClaimService claimService;

    public void setUserWorkgroupService(UserWorkgroupService userWorkgroupService) {
        this.userWorkgroupService = userWorkgroupService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public Workgroup getWorkgroup(int id) {
        return (Workgroup) get(Workgroup.class, id);
    }

    public void delete(Workgroup object) {
        delete(object);
    }

    public void save(Workgroup object) {
        save(object);
    }

    public List<Workgroup> getWorkgroupsByInsurer(int insurerId) {

        List<Workgroup> objects = new ArrayList<Workgroup>();

        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        if (insurerId > 0) {
            criteria.add(Restrictions.eq("insurer.id", insurerId));
        }
        criteria.add(Restrictions.eq("status", true));
        criteria.addOrder(Order.asc("name"));
        objects = findByCriteria(criteria);

        return objects;
    }

    public List<Workgroup> getAllWorkgroupsByInsurer(int insurerId, String strOrder) {

        List<Workgroup> objects = new ArrayList<Workgroup>();

        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);

        if (insurerId > 0) {
            criteria.add(Restrictions.eq("insurer.id", insurerId));
        }

        if (strOrder.equalsIgnoreCase("")) {
            criteria.addOrder(Order.asc(strOrder));
        }

        objects = findByCriteria(criteria);

        return objects;
    }

    public boolean isWorkgroupNameExistByInsurer(int insurerId, String workgroupName) {

        boolean isExist = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("name", workgroupName.trim()));

        if (findByCriteria(criteria).size() > 0) {
            isExist = true;
        }

        return isExist;

    }

    public void defaultWorkgroup(Insurer insurer) {
        Workgroup object = new Workgroup();
        object.setInsurer(insurer);
        object.setName(insurer.getName());
        object.setStatus(true);
        save(object);
    }

    public List<Workgroup> getAvailableWorkgroupsByInsurer(int insurerId, int webUserId) {

        // GET ALL WORKGROUPS BY INSURER
        DetachedCriteria workgroupCirteria = DetachedCriteria.forClass(Workgroup.class);
        workgroupCirteria.add(Restrictions.eq("insurer.id", insurerId));

        // GET ALL WORKGROUPS ASSIGNED TO WEB USER
        DetachedCriteria userworkgroupCirteria = DetachedCriteria.forClass(WebUserWorkgroup.class);
        userworkgroupCirteria.add(Restrictions.eq("user.id", webUserId));
        userworkgroupCirteria.setProjection(Property.forName("workgroup.id"));

        // FILTERED BY ASSIGNED WORKGROUPS
        workgroupCirteria.add(Property.forName("id").notIn(userworkgroupCirteria));

        // RETURN SEARCH RESULT
        return findByCriteria(workgroupCirteria);
        
    }

    public boolean isWorkgroupDeletable(int workgroupId) {

        boolean isExist = false;

        if (!this.userWorkgroupService.isUserWorkgroupExist(workgroupId, null) && !this.claimService.isObjectExist(workgroupId)) {
            isExist = true;
        }

        return isExist;
    }

    public boolean isWorkgroupAllowToInactive(int insurerId, int workgroupId) {

        boolean isAllowed = false;

        List<Workgroup> objects = new ArrayList<Workgroup>();
        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.ne("id", workgroupId));
        criteria.add(Restrictions.eq("status", true));
        objects = findByCriteria(criteria);

        if (objects.size() > 0) {
            isAllowed = true;
        }

        return isAllowed;
    }

    public boolean isInsurerAllowToEnableWorkgroup(Insurer insurer) {

        boolean isAllowed = false;

        if (insurer != null) {

            List<Workgroup> objects = new ArrayList<Workgroup>();
            DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
            criteria.add(Restrictions.eq("insurer.id", insurer.getId()));
            criteria.add(Restrictions.eq("status", true));
            objects = findByCriteria(criteria);

            if (objects.size() > 0) {
                isAllowed = true;
            }
        }

        return isAllowed;
    }
}
