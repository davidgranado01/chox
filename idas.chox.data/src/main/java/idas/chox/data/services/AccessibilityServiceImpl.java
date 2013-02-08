package idas.chox.data.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Accessibility;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.AccessibilityService;

public class AccessibilityServiceImpl extends BaseDataService implements AccessibilityService {
    private static final Logger LOG = LoggerFactory.getLogger(AccessibilityServiceImpl.class);


    @Override
    public Map<String, List<Accessibility>> getBatchUpdateAccessibilityMap() {
        Map<String, List<Accessibility>> batchUpdateAccessibilityMap = new HashMap<String, List<Accessibility>>(10);
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        c.add(Restrictions.like("name", "batch.%"));
        List<Accessibility> accessibilities = findByCriteria(c);
        // Now split into a map with the key on 'batch.<action name>'
        for (Accessibility a : accessibilities) {
            String key = a.getName().substring(0, a.getName().lastIndexOf('.'));
            if (batchUpdateAccessibilityMap.containsKey(key)) {
                List<Accessibility> access = batchUpdateAccessibilityMap.remove(key);
                access.add(a);
                batchUpdateAccessibilityMap.put(key, access);
            } else {
                List<Accessibility> access = new ArrayList<Accessibility>();
                access.add(a);
                batchUpdateAccessibilityMap.put(key, access);
            }
        }

        if (batchUpdateAccessibilityMap.size() > 10) {
            LOG.error("Please update initial batchUpdateAccessibilityMap size: current size=10, should be {}", batchUpdateAccessibilityMap.size());
        }
        return batchUpdateAccessibilityMap;
    }


    @Override
    public Map<String, Accessibility> getAccessibilityByClaimTypeMap() {

        Map<String, Accessibility> map = new HashMap<String, Accessibility>(14000);
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        // Only interested in actions that are broken down by claim type
        c.add(Restrictions.disjunction().add(Restrictions.like("name", "batch.%"))
        .add(Restrictions.like("name", "action.%"))
        .add(Restrictions.like("name", "extraAction.%"))
        .add(Restrictions.like("name", "notification.%"))
        .add(Restrictions.like("name", "button.%"))
        .add(Restrictions.like("name", "tab.%")));
        List<Accessibility>  accessibilities = findByCriteria(c);

        for (Accessibility a : accessibilities) {

            if (a.getClaimType() == null) {
                // Valid for all claim types
                for (ClaimType type : ClaimType.values()) {
                    map.put(a.getName() + "." + type.name(), a);
                }
                map.put(a.getName() + ".ALL", a);
            } else {
                map.put(a.getName() + "." + a.getClaimType().name(), a);
            }
        }

        if (map.size() > 10) {
            LOG.error("Please update initial AccessibilityByClaimTypeMap size: current init size=14000, shuld be {}", map.size());
        }
        return map;
    }


    @Override
    public Map<String, Accessibility> getAccessibilityMap() {

        Map<String, Accessibility> map = new HashMap<String, Accessibility>(100);
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        // Not interested in actions that are broken down by claim type
        c.add(Restrictions.conjunction().add(Restrictions.not(Restrictions.like("name", "batch.%")))
         .add(Restrictions.not(Restrictions.like("name", "action.%")))
         .add(Restrictions.not(Restrictions.like("name", "extraAction.%")))
         .add(Restrictions.not(Restrictions.like("name", "notification.%")))
         .add(Restrictions.not(Restrictions.like("name", "button.%")))
         .add(Restrictions.not(Restrictions.like("name", "tab.%"))));

        List<Accessibility> accessibilities = findByCriteria(c);

        for (Accessibility a : accessibilities) {
            map.put(a.getName(), a);
        }

        if (map.size() > 10) {
            LOG.error("Please update initial AccessibilityMap size: current init size=100, shuld be {}", map.size());
        }
        return map;
    }

}
