package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.AutomaticRoutingPolicy;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UserWorkgroupService;
import idas.chox.core.services.WorkgroupService;

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

    @Override
    public Workgroup getWorkgroup(int workgroupId) {
        return (Workgroup) get(Workgroup.class, workgroupId);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteWorkgroup(Workgroup workgroup) {
        delete(workgroup);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveWorkgroup(Workgroup workgroup) {
        save(workgroup);
    }

    @Override
    public List<Workgroup> getActiveWorkgroupsByInsurer(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("status", true));
        criteria.addOrder(Order.asc("name"));
        
        return findByCriteria(criteria);
    }

    @Override
    public List<Workgroup> getActiveWorkgroupsByInsurerSortByNoClaims(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("status", true));
        criteria.add(Restrictions.sqlRestriction("id = (select id from (select w.id, count(*) as noClaims from workgroup w, claim c "
                + "where c.workgroup_id = w.id and c.insurer_id = " + insurerId + " and w.status = true "
                + " and c.status not in ('ClaimClosed','ClaimRejectionAccepted','InvoiceRejectionAccepted','PaymentReceived','AwaitingLitigationOutcome','InvoicePaymentLogged','AwaitingInvoicePayment') "
                + " group by w.id order by noClaims asc limit 1) tbl)"));
        
        return findByCriteria(criteria);
    }

    @Override
    public List<Workgroup> getWorkgroupsByInsurer(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.addOrder(Order.asc("name"));
        
        return findByCriteria(criteria);
    }

    @Override
    public boolean isWorkgroupNameExistByInsurer(int insurerId, String workgroupName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("name", workgroupName.trim()));

        return findByCriteria(criteria).size() > 0;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void createDefaultWorkgroup(Insurer insurer) {
        Workgroup object = new Workgroup();
        object.setInsurer(insurer);
        object.setName(insurer.getName());
        object.setSite(insurer.getName());
        object.setTeam(insurer.getName());
        object.setStatus(true);
        saveWorkgroup(object);
    }

    @Override
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

    @Override
    public List<Workgroup> getAvailableAutoRoutingWorkgroupsByInsurer(int insurerId, boolean isActiveOnly) {
        // GET ALL WORKGROUPS BY INSURER
        DetachedCriteria workgroupCirteria = DetachedCriteria.forClass(Workgroup.class);
        workgroupCirteria.add(Restrictions.eq("insurer.id", insurerId));
        
        if (isActiveOnly) {
            workgroupCirteria.add(Restrictions.eq("status", true));
        }

        // GET ALL WORKGROUPS ASSIGNED TO WEB USER
        DetachedCriteria autoroutingworkgroupCirteria = DetachedCriteria.forClass(AutomaticRoutingPolicy.class);
        autoroutingworkgroupCirteria.add(Restrictions.eq("insurer.id", insurerId));
        autoroutingworkgroupCirteria.setProjection(Property.forName("workgroup.id"));

        // FILTERED BY ASSIGNED WORKGROUPS
        workgroupCirteria.add(Property.forName("id").notIn(autoroutingworkgroupCirteria));

        // RETURN SEARCH RESULT
        return findByCriteria(workgroupCirteria);
    }

    @Override
    public boolean isWorkgroupDeletable(int workgroupId) {

        return !isWorkgroupInUseByUser(workgroupId) && !this.claimService.isObjectExist(workgroupId)
                && !automaticRoutingService.isWorkgroupInUseByAutomaticRouting(workgroupId);
    }

    @Override
    public boolean isWorkgroupAllowToInactive(int insurerId, int workgroupId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.ne("id", workgroupId));
        criteria.add(Restrictions.eq("status", true));

        return (findByCriteria(criteria)).size() > 0;
    }

    @Override
    public boolean isInsurerWithWorkgroup(int insurerId) {
        List<Workgroup> objects;
        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("status", true));
        objects = findByCriteria(criteria);
        
        return objects.size() > 0;
    }

    private boolean isWorkgroupInUseByUser(Integer workgroupId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserWorkgroup.class);
        criteria.add(Restrictions.eq("workgroup.id", workgroupId));
        
        return findByCriteria(criteria).size() > 0;
    }
}
