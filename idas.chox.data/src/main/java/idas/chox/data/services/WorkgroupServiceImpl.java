package idas.chox.data.services;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UserWorkgroupService;
import idas.chox.core.services.WorkgroupService;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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

    public List<Workgroup> getObjects(int insurerId) {

        List<Workgroup> objects = new ArrayList<Workgroup>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);

            if (insurerId > 0) {
                criteria.add(Restrictions.eq("insurer.id", insurerId));
            }

            criteria.add(Restrictions.eq("status", true));
            criteria.addOrder(Order.asc("name"));

            objects = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return objects;
    }

    public List<Workgroup> getAllObjects(int insurerId) {

        List<Workgroup> objects = new ArrayList<Workgroup>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);

            if (insurerId > 0) {
                criteria.add(Restrictions.eq("insurer.id", insurerId));
            }

            criteria.addOrder(Order.asc("name"));

            objects = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return objects;
    }

    public Workgroup getObject(int id) {
        return (Workgroup) get(Workgroup.class, id);
    }

    public boolean DeleteObject(Workgroup object) {

        boolean bFlag = false;

        try {

            delete(object);

            bFlag = true;

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return bFlag;
    }

    public void updateObject(Workgroup object) {
        save(object);
    }

    public boolean isWorkgroupExist(int insurerId, String workgroupName) {

        boolean isExist = false;

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);

            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("name", workgroupName.trim()));

            if (findByCriteria(criteria).size() > 0) {
                isExist = true;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return isExist;

    }

    public boolean isWorkgroupDeletable(int workgroupId) {

        boolean isExist = false;

        try {

            if (!this.userWorkgroupService.isObjectExist(workgroupId) && !this.claimService.isObjectExist(workgroupId)) {
                isExist = true;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return isExist;
    }

    public boolean isWorkgroupAllowToInactive(int insurerId, int workgroupId) {

        boolean isAllowed = false;

        try {

            List<Workgroup> objects = new ArrayList<Workgroup>();
            DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.ne("id", workgroupId));
            criteria.add(Restrictions.eq("status", true));
            objects = findByCriteria(criteria);

            // System.out.println("objects:"+objects);
            // System.out.println("objects size:"+objects.size());

            if (objects.size() > 0) {
                isAllowed = true;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return isAllowed;
    }

    public boolean isInsurerAllowToEnableWorkgroup(Insurer insurer) {

        boolean isAllowed = false;

        try {

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

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return isAllowed;
    }

    public void createDefaultRecord(Insurer insurer) {
        Workgroup object = new Workgroup();
        object.setInsurer(insurer);
        object.setName(insurer.getName());
        object.setStatus(true);
        updateObject(object);
    }
}
