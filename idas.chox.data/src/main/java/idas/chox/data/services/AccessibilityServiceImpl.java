package idas.chox.data.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import idas.chox.core.model.Accessibility;
import idas.chox.core.model.AccessibilityItem;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.AccessibilityService;

public class AccessibilityServiceImpl extends BaseDataService implements AccessibilityService {


    @Override
    public Map<String, List<Accessibility>> getBatchUpdateAccessibilityMap() {
        Map<String, List<Accessibility>> batchUpdateAccessibilityMap = new HashMap<String, List<Accessibility>>(120);
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        c.add(Restrictions.like("name", "batch.%"));
        List<Accessibility> accessibilities = findByCriteria(c);
        // Now split into a map with the key on 'batch.<action name>'
        for (Accessibility a : accessibilities) {
            for (Object item : a.getAccessibilityItem()) {
                // Just force the loading of the items to prevent lazy loading
                // exception later
            }
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

        return batchUpdateAccessibilityMap;
    }


    @Override
    public Map<String, Object[]> getAccessibilityByClaimTypeMap() {

        HashMap<String, Object[]> map = new HashMap<String, Object[]>(10000);
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

            HashMap roleMap = new HashMap();
            for (Object item : a.getAccessibilityItem()) {

                AccessibilityItem aItem = (AccessibilityItem) item;
                roleMap.put(aItem.getRole().trim(), aItem.getAccessRight());
            }
            if (a.getClaimType() == null) {
                // Valid for all claim types
                for (ClaimType type : ClaimType.values()) {
                    map.put(a.getName() + "." + type.name(), new Object[]{a,roleMap});
                }
                map.put(a.getName() + ".ALL", new Object[]{a,roleMap});
            } else {
                map.put(a.getName() + "." + a.getClaimType().name(), new Object[]{a,roleMap});
            }
        }

        return map;
    }

    @Override
    public Map<String, Object[]> getAccessibilityMap() {

        HashMap<String, Object[]> map = new HashMap<String, Object[]>(1000);
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

            HashMap roleMap = new HashMap();
            for (Object item : a.getAccessibilityItem()) {

                AccessibilityItem aItem = (AccessibilityItem) item;
                roleMap.put(aItem.getRole().trim(), aItem.getAccessRight());
            }
            map.put(a.getName(), new Object[]{a,roleMap});
        }

        return map;
    }

}
