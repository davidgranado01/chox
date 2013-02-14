package idas.chox.data.services;

import idas.chox.core.model.*;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.ReasonOfRejectionService;
import idas.chox.core.util.LookupItemTextComparator;
import idas.chox.core.util.RoleHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LookupServiceImpl extends SecureDataService implements LookupService, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(LookupServiceImpl.class);
    
    private ReasonOfRejectionService reasonOfRejectionService;

    @Override
    public List<LookupItem> getStatuses(boolean isWorkgroupEnabled, boolean isClaimOwnershipEnabled,
            boolean isFnolEnabled, boolean isEngineersEnabled, boolean isTpiEnabled,
            boolean isManualInvoiceAllowed, boolean isSubscriberActivated) {
        List<LookupItem> items = new ArrayList<LookupItem>();
        for (String s : ClaimStatus.getAvailableStatus(isWorkgroupEnabled, isClaimOwnershipEnabled,
                isFnolEnabled, isEngineersEnabled,isTpiEnabled, isManualInvoiceAllowed, isSubscriberActivated)) {
            items.add(new LookupItem(s, s));
        }
        Collections.sort(items,new LookupItemTextComparator());
        return items;
    }

    @Override
    public List<LookupItem> getLiabilityStatuses(boolean withNull) {
        List<LookupItem> items = new ArrayList<LookupItem>();
        for (LiabilityStatus s : LiabilityStatus.values()) {
            if (!withNull && s.getLiablityValue() == 0) continue;
            else if (withNull && s.getLiablityValue() == 0)
                items.add(new LookupItem("<i>(Not Specified)</i>", Integer.toString(s.getLiablityValue())));
            else
                items.add(new LookupItem(s.toString(), Integer.toString(s.getLiablityValue())));
        }
        return items;
    }

    @Override
    public List<LookupItem> getClaimTypes() {
        List<LookupItem> items = new ArrayList<LookupItem>();
        
        items.add(new LookupItem(ClaimType.GTA.toString(), Integer.toString(ClaimType.GTA.getClaimTypeValue())));
        items.add(new LookupItem(ClaimType.SUBSCRIBER.toString(), Integer.toString(ClaimType.SUBSCRIBER.getClaimTypeValue())));
        items.add(new LookupItem(ClaimType.FIXED_FEE.toString(), Integer.toString(ClaimType.FIXED_FEE.getClaimTypeValue())));
        items.add(new LookupItem(ClaimType.TPI.toString(), Integer.toString(ClaimType.TPI.getClaimTypeValue())));
        items.add(new LookupItem(ClaimType.INSURER_VS_INSURER.toString(), Integer.toString(ClaimType.INSURER_VS_INSURER.getClaimTypeValue())));
        items.add(new LookupItem(ClaimType.INSURER_INVOICE.toString(), Integer.toString(ClaimType.INSURER_INVOICE.getClaimTypeValue())));
        
        return items;
    }

    @Override
    public List getVehicleClasses() {
        DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClass.class);
       // criteria.add(Restrictions.ne("name", "UNATTACHED"));
        criteria.addOrder(Order.asc("name"));
        return findByCriteria(criteria, true);
    }

    @Override
    public String getVehicleClassName(int id) {


        List<VehicleClass> vehicleClasses = getVehicleClasses();
        String name = null;
        for (VehicleClass vClass : vehicleClasses) {
            if (vClass.getId() == id) {

                name = vClass.getName();
                break;
            }
        }
        return name;
    }

    @Override
    public List<ReasonOfRejection> getClaimRejectionReason(int insurerId, ClaimType claimType) {
    	List<ReasonOfRejection> rejectionReasons =  reasonOfRejectionService.getInsurerReasonsOfRejection(insurerId, ReasonOfRejection.TYPE_CLAIM , claimType, true, null);
        return rejectionReasons;
    }
    
    @Override
    public List<ReasonOfRejection> getClaimRejectionRestrictedReason(int insurerId, ClaimType claimType) {
    	List<ReasonOfRejection> rejectionReasons =  reasonOfRejectionService.getInsurerReasonsOfRejection(insurerId, ReasonOfRejection.TYPE_CLAIM, claimType, true, true);
        return rejectionReasons;
    }

    @Override
    public List<ReasonOfRejection> getInvoiceRejectionReason(int insurerId, ClaimType claimType) {
    	List<ReasonOfRejection> rejectionReasons =  reasonOfRejectionService.getInsurerReasonsOfRejection(insurerId, ReasonOfRejection.TYPE_INVOICE, claimType, true, null);
        return rejectionReasons;
    }
    
    @Override
    public List getInsurerChoBand(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(BreBand.class).addOrder(Order.asc("id"));
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        return findByCriteria(criteria, true);
    }

    @Override
    public List getNonProvisionReason() {
        List items = new ArrayList<LookupItem>();
        items.add(new LookupItem("Point Blank Refusal", "Point Blank Refusal"));
        items.add(new LookupItem("Faxed Garage", "Faxed Garage"));
        /*
         * when  ( items.add(new LookupItem("Information Not Available/No System Access", "Info. Not Available/No System Access")); ) changed
         * please change in  isHireMonitoringLabourDetailExist(Claim claim) method in ClaimAwaitingCarHireInfo class.
         * where this is set as String manually.
         */
        items.add(new LookupItem("Information Not Available/No System Access", "Info. Not Available/No System Access"));
        items.add(new LookupItem("Non Contactable/Ring Through", "Non Contactable/Ring Through"));
        items.add(new LookupItem("Update Obtained By Other Source", "Update Obtained By Other Source"));
        return items;
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // LINE OF REASON OF DELAY
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public List getReasonOfDelay() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfDelay.class);
        criteria.add(Restrictions.eq("status", true));
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria, true);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // WORKGROUP LIST
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public List getWorkgroups(WebUser user, boolean isActiveOnly) {

        List workgroups = new ArrayList();

        if (RoleHelper.isChoxAdmin(user)) {
            workgroups = getAllWorkgroup(isActiveOnly);
        } else {

            if (RoleHelper.isInsurerUser(user)) {

                if (RoleHelper.isWorkgroupRelatedUserOnly(user)) {
                    workgroups = getWorkgroupsByUserId(user.getId(), isActiveOnly);
                } else {
                    workgroups = getWorkgroupsByInsurerId(user.getInsurer().getId(), isActiveOnly);
                }
            }
        }

        return workgroups;

    }

    private List<Workgroup> getAllWorkgroup(boolean isActiveOnly) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);

        if (isActiveOnly) {
            criteria.add(Restrictions.eq("status", true));
        }

        criteria.addOrder(Order.asc("name"));
        return findByCriteria(criteria, true);

    }

    @Override
    public List<Workgroup> getWorkgroupsByInsurerId(int insurerId, boolean isActiveOnly) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);

        if (isActiveOnly) {
            criteria.add(Restrictions.eq("status", true));
        }

        if (insurerId > 0) {
            criteria.add(Restrictions.eq("insurer.id", insurerId));
        }

        criteria.addOrder(Order.asc("name"));
        return findByCriteria(criteria, true);
    }

    private List<Workgroup> getWorkgroupsByUserId(int userId, boolean isActiveOnly) {

        List<Workgroup> workgroups = new ArrayList<Workgroup>();

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserWorkgroup.class);
        criteria.add(Restrictions.eq("user.id", userId));
        List result = findByCriteria(criteria, true);

        for (Object o : result) {

            WebUserWorkgroup a = (WebUserWorkgroup) o;

            if (isActiveOnly) {
                if (a.getWorkgroup().isStatus()) {
                    workgroups.add((Workgroup) a.getWorkgroup());
                }
            } else {
                workgroups.add((Workgroup) a.getWorkgroup());
            }
        }

        return workgroups;
    }

    @Override
    public List<Workgroup> getWorkgroupsByClaimId(int claimId, boolean isActiveOnly) {


        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        criteria.add(Restrictions.eq("id", claimId));
        Claim claim = (Claim) getByCriteria(criteria);

        return getWorkgroupsByInsurerId(claim.getInsurer().getId(), isActiveOnly);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // SUPPLIERS / CREDIT HIRES
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public List<Chorganisation> getSuppliers(boolean excludeManualCHO) {

        WebUser currentUser = getCurrentUser();

        if (currentUser.isCHOXAdmin()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("status", true));
            return findByCriteria(criteria, true);

        } else if (currentUser.isAnInsurer()) {
            return getSuppliers(currentUser.getInsurer().getId(),excludeManualCHO);
        } else {
            LOG.error("Trying to get suppliers for a CHO user ({})", currentUser.getDisplayName());
            return new ArrayList<Chorganisation>();
        }
    }
    
    @Override
    public List<Chorganisation> getAllSuppliers() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
        criteria.addOrder(Order.asc("name"));
        return findByCriteria(criteria, true);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // INSURERS
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public List<Insurer> getAllInsurers() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
        criteria.addOrder(Order.asc("name"));
        return findByCriteria(criteria, true);
    }

    @Override
    public List<Insurer> getInsurers() {

        WebUser currentUser = getCurrentUser();

        if (currentUser.isCHOXAdmin()) {

            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.add(Restrictions.eq("status", true));
            criteria.addOrder(Order.asc("name"));
            return findByCriteria(criteria, true);

        } else if (!currentUser.isAnInsurer()){
            return getInsurers(currentUser.getChorganisation().getId());
        } else {
            LOG.error("Trying to get insurers for an Insurer user ({})", currentUser.getDisplayName());
            return new ArrayList<Insurer>();
        }
    }


    @Override
    public List<String> getSitesByInsurerId(int insurerId, boolean isActiveOnly) {
        LOG.debug("Getting sites for insurerId={}", insurerId);
        List<String> sites = null;
        try {

            sites = new ArrayList<String>();

            StringBuilder sb = new StringBuilder();
            sb.append("select distinct site from workgroup where insurer_id=:pInsurerId ");
            if (isActiveOnly) {
                sb.append("and status=true ");
            }
            sb.append("order by site");
            Map extParameters = new HashMap();

            extParameters.put("pInsurerId", insurerId);

            List result = externalQuery(sb.toString(), extParameters);

            for (Object o : result) {
                Map data = (Map) o;
                LOG.debug("Adding site '{}'", (String) data.get("site"));
                sites.add((String) data.get("site"));
            }

        } catch (Exception ex) {
            LOG.error("Exception caught getting Sites for Insurer with ID={}: {}", insurerId, ex.getMessage());
        }
        return sites;
    }

    @Override
    public List<String> getTeamsBySite(int insurerId, String site, boolean isActiveOnly) {
        LOG.debug("Getting teams for insurerId={} and site='{}'", insurerId, site);
        List<String> teams = null;
        try {
            Map extParameters = new HashMap();

            teams = new ArrayList<String>();

            StringBuilder sb = new StringBuilder();
            sb.append("select distinct team from workgroup where insurer_id=:pInsurerId ");
            if (isActiveOnly) {
                sb.append("and status=true ");
            }
            if (site != null && site.length() > 0) {
                sb.append("and site=:pSite ");
                extParameters.put("pSite", site);
            }
            sb.append("order by team");


            extParameters.put("pInsurerId", insurerId);

            List result = externalQuery(sb.toString(), extParameters);

            for (Object o : result) {
                Map data = (Map) o;
                LOG.debug("Adding team '{}'", (String) data.get("team"));
                teams.add((String) data.get("team"));
            }

        } catch (Exception ex) {
            LOG.error("Exception caught getting Teams by Site for Insurer with ID={}: {}", insurerId, ex.getMessage());
        }
        return teams;
    }

	public ReasonOfRejectionService getReasonOfRejectionService() {
		return reasonOfRejectionService;
	}

	public void setReasonOfRejectionService(ReasonOfRejectionService reasonOfRejectionService) {
		this.reasonOfRejectionService = reasonOfRejectionService;
	}

    
    @Override
    public List<Insurer> getInsurers(Integer choId) {

        List<Insurer> results = new ArrayList<Insurer>();

        if (choId == null) {
            LOG.error("Cannot get insurers for null choId.");
            return results;
        }


        try {

            List result;

            StringBuilder sb = new StringBuilder();
            sb.append("select a.id as id, a.name as name from insurer ");
            sb.append("a inner join insurer_chorganisation b on a.id = b.insurer_id ");
            sb.append("where a.status=true and b.chorganisation_id=:pChorganisationId order by a.name");

            Map extParameters = new HashMap();

            extParameters.put("pChorganisationId", choId);

            result = externalQuery(sb.toString(), extParameters, IdLookupItem.class);

            for (Object o : result) {
                IdLookupItem data = (IdLookupItem) o;
                Insurer item = new Insurer();
                item.setId(data.getId());
                item.setName(data.getName());
                results.add(item);
            }

        } catch (Exception ex) {
            LOG.error("Exception caught getting Insurers for CHO with ID={}: {}", choId, ex.getMessage());
        }

        return results;

    }
    
    @Override
    public List<Chorganisation> getSuppliers(Integer insurerId, boolean excludeManualCHO) {

        List<Chorganisation> results = new ArrayList<Chorganisation>();

        if (insurerId == null) {
            LOG.error("Cannot get suppliers for null insurerId.");
            return results;
        }

        try {

            List result;

            StringBuilder sb = new StringBuilder();
            sb.append("select a.id as id, a.name as name from chorganisation ");
            sb.append("a inner join insurer_chorganisation b on a.id = b.chorganisation_id ");
            sb.append("where a.status=true and b.insurer_id=:pInsurerId ");
            if (excludeManualCHO) {
                sb.append("and a.insurer_upload_only=false ");
            }
            sb.append("order by a.name");

            Map extParameters = new HashMap();
            extParameters.put("pInsurerId", insurerId);
            result = externalQuery(sb.toString(), extParameters, IdLookupItem.class);

            for (Object o : result) {
                IdLookupItem data = (IdLookupItem) o;
                Chorganisation item = new Chorganisation();
                item.setId(data.getId());
                item.setName(data.getName());
                results.add(item);
            }

        } catch (Exception ex) {
            LOG.error("Exception caught getting suppliers for insurer with ID={}: {}", insurerId, ex.getMessage());
        }

        return results;
    }

}
