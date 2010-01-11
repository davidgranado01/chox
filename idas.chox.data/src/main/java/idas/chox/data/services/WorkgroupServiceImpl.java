package idas.chox.data.services;

import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UserWorkgroupService;
import idas.chox.core.services.WorkgroupService;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class WorkgroupServiceImpl extends SecureDataService implements WorkgroupService {

    protected UserWorkgroupService userWorkgroupService;
    protected AutomaticRoutingService automaticRoutingService;
    protected ClaimService claimService;

    public void setUserWorkgroupService(UserWorkgroupService userWorkgroupService) {
        this.userWorkgroupService = userWorkgroupService;
    }

    public void setAutomaticRoutingService(AutomaticRoutingService automaticRoutingService) {
        this.automaticRoutingService = automaticRoutingService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public Workgroup getWorkgroup(int workgroupId) {
        return (Workgroup) get(Workgroup.class, workgroupId);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteWorkgroup(Workgroup workgroup) {
        delete(workgroup);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveWorkgroup(Workgroup workgroup) {
        save(workgroup);
    }

    public List<Workgroup> getActiveWorkgroupsByInsurer(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("status", true));
        criteria.addOrder(Order.asc("name"));
        return findByCriteria(criteria);
    }

    public List<Workgroup> getWorkgroupsByInsurer(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.addOrder(Order.asc("name"));
        return findByCriteria(criteria);
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createDefaultWorkgroup(Insurer insurer) {
        Workgroup object = new Workgroup();
        object.setInsurer(insurer);
        object.setName(insurer.getName());
        object.setStatus(true);
        saveWorkgroup(object);
    }

    public List<Workgroup> getAvailableUserWorkgroupsByInsurer(int insurerId, int webUserId) {

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

    public List<Workgroup> getAvailableAutoRoutingWorkgroupsByInsurer(int insurerId) {

        // GET ALL WORKGROUPS BY INSURER
        DetachedCriteria workgroupCirteria = DetachedCriteria.forClass(Workgroup.class);
        workgroupCirteria.add(Restrictions.eq("insurer.id", insurerId));

        // GET ALL WORKGROUPS ASSIGNED TO WEB USER
        DetachedCriteria autoroutingworkgroupCirteria = DetachedCriteria.forClass(AutomaticRouting.class);
        autoroutingworkgroupCirteria.add(Restrictions.eq("insurer.id", insurerId));
        autoroutingworkgroupCirteria.setProjection(Property.forName("workgroup.id"));

        // FILTERED BY ASSIGNED WORKGROUPS
        workgroupCirteria.add(Property.forName("id").notIn(autoroutingworkgroupCirteria));

        // RETURN SEARCH RESULT
        return findByCriteria(workgroupCirteria);

    }

    public boolean isWorkgroupDeletable(int workgroupId) {

        boolean isExist = false;

        if (!isWorkgroupInUseByUser(workgroupId) && !this.claimService.isObjectExist(workgroupId) && !automaticRoutingService.isWorkgroupInUseByAutomaticRouting(workgroupId)) {
            isExist = true;
        }

        return isExist;
    }

    public boolean isWorkgroupAllowToInactive(int insurerId, int workgroupId) {
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.ne("id", workgroupId));
        criteria.add(Restrictions.eq("status", true));

        if ((findByCriteria(criteria)).size() > 0) {
            return true;
        }

        return false;
    }

    public boolean isInsurerWithWorkgroup(int insurerId) {
        List<Workgroup> objects = new ArrayList<Workgroup>();
        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("status", true));
        objects = findByCriteria(criteria);
        if (objects.size() > 0) {
            return true;
        }
        return false;
    }

    private boolean isWorkgroupInUseByUser(Integer workgroupId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserWorkgroup.class);
        criteria.add(Restrictions.eq("workgroup.id", workgroupId));
        if (findByCriteria(criteria).size() > 0) {
            return true;
        }
        return false;
    }
}
