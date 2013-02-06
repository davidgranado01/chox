package idas.chox.data.services;

import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import idas.chox.core.model.Accessibility;
import idas.chox.core.model.AccessibilityItem;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.AccessibilityService;
import org.hibernate.criterion.Order;

public class AccessibilityServiceImpl extends BaseDataService implements AccessibilityService {

    @Override
    public List<Accessibility> getBatchUpdateAccessibilityMap(String accessibilityKey) {
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        c.add(Restrictions.like("name", accessibilityKey + ".%"));
        return findByCriteria(c);
    }

    @Override
    public HashMap getAccessibilityKeyMap() {

        HashMap<String, ClaimType> map = new HashMap<String, ClaimType>();
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        List result = findByCriteria(c);

        for (Object o : result) {
            Accessibility a = (Accessibility) o;
            map.put(a.getName(), a.getClaimType());
        }

        return map;
    }

    @Override
    public HashMap<String, HashMap> getAccessibilityMap() {

        HashMap<String, HashMap> map = new HashMap<String, HashMap>();
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        List result = findByCriteria(c);

        for (Object o : result) {
            Accessibility a = (Accessibility) o;

            HashMap roleMap = new HashMap();
            for (Object item : a.getAccessibilityItem()) {

                AccessibilityItem aItem = (AccessibilityItem) item;
                roleMap.put(aItem.getRole().trim(), aItem.getAccessRight());
            }
            String name;
            if (a.getClaimType() == null) {
                // Valid for all claim types
                for (ClaimType type : ClaimType.values()) {
                    map.put(a.getName() + "." + type.name(), roleMap);
                }
                map.put(a.getName() + ".ALL", roleMap);
            } else {
                map.put(a.getName() + "." + a.getClaimType().name(), roleMap);
            }
        }

        return map;
    }

    @Override
    public Accessibility getAccessibility(String accessibilityKey, ClaimType claimType) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Accessibility.class);
        criteria.add(Restrictions.eq("name", accessibilityKey));
        if (claimType == null) {
            criteria.add(Restrictions.isNull("claimType"));
        } else {
            criteria.add(Restrictions.disjunction().add(Restrictions.eq("claimType", claimType))
                                                   .add(Restrictions.isNull("claimType")))
                    .addOrder(Order.asc("claimType"));
        }
        return (Accessibility) getByCriteria(criteria);
    }

    private int getAccessibilityId(String accessibilityKey) {

        int oResult = -1;

        DetachedCriteria criteria = DetachedCriteria.forClass(Accessibility.class);
        criteria.add(Restrictions.eq("name", accessibilityKey));
        Accessibility object = (Accessibility) getByCriteria(criteria);

        if (object != null) {
            oResult = object.getId();
        }

        return oResult;
    }
    }
