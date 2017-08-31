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
import java.text.MessageFormat;

public class AccessibilityServiceImpl extends BaseDataService implements AccessibilityService {
    private static final Logger LOG = LoggerFactory.getLogger(AccessibilityServiceImpl.class);
    private static final int BATCH_UPDATE_ACCESSIBILITY_MAP_SIZE = 10;
    private static final int ACCESSIBILITY_BY_CLAIMTYPE_MAP_SIZE = 12000;
    private static final int ACCESSIBILITY_MAP_SIZE = 100;

    
    private void addRolesToAccessibility(Accessibility a1, Accessibility a2) {
        Map<String, Short> roleMap = a1.getAccessibilityRoleMap();
        roleMap.putAll(a2.getAccessibilityRoleMap());
    }


    @Override
    public Map<String, List<Accessibility>> getBatchUpdateAccessibilityMap() {
        Map<String, List<Accessibility>> batchUpdateAccessibilityMap = new HashMap<String, List<Accessibility>>(BATCH_UPDATE_ACCESSIBILITY_MAP_SIZE);
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        c.add(Restrictions.like("name", "batch.%"));
        List<Accessibility> accessibilities = findByCriteria(c);
        // Now split into a map with the key on 'batch.<action name>'
        for (Accessibility a : accessibilities) {
            this.evict(a);
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
        LOG.info("BatchUpdateAccessibilityMap loaded: {} entries", batchUpdateAccessibilityMap.size());

        return batchUpdateAccessibilityMap;
    }


    @Override
    public Map<String, Accessibility> getAccessibilityByClaimTypeMap() {

        Map<String, Accessibility> map = new HashMap<String, Accessibility>(ACCESSIBILITY_BY_CLAIMTYPE_MAP_SIZE);
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        // Only interested in actions that are broken down by claim type
        c.add(Restrictions.disjunction()
                .add(Restrictions.like("name", "activity.%"))
                .add(Restrictions.like("name", "batch.%"))
                .add(Restrictions.like("name", "extraAction.%"))
                .add(Restrictions.like("name", "notification.%"))
                .add(Restrictions.like("name", "tab.%")));
        List<Accessibility>  accessibilities = findByCriteria(c);

        String key;
        for (Accessibility a : accessibilities) {
            this.evict(a);
            if (a.getClaimType() == null) {
                // Valid for all claim types
                key = MessageFormat.format("{0}.{1}", a.getName(), ClaimType.GTA.name());
                if (map.containsKey(key)) {
                    addRolesToAccessibility(map.get(key), a);                    
                } else {
                    map.put(key, a);
                    // Accessibility has been used - we now need to duplicate
                    a = new Accessibility(a);
                }
                key = MessageFormat.format("{0}.{1}", a.getName(), ClaimType.COLLABORATION_PROTOCOL.name());
                if (map.containsKey(key)) {
                    addRolesToAccessibility(map.get(key), a);                    
                } else {
                    map.put(key, a);
                    a = new Accessibility(a);
                }
                key = MessageFormat.format("{0}.{1}", a.getName(), ClaimType.SUBSCRIBER.name());
                if (map.containsKey(key)) {
                    addRolesToAccessibility(map.get(key), a);                    
                } else {
                    map.put(key, a);
                    a = new Accessibility(a);
                }
                key = MessageFormat.format("{0}.{1}", a.getName(), ClaimType.FIXED_FEE.name());
                if (map.containsKey(key)) {
                    addRolesToAccessibility(map.get(key), a);                    
                } else {
                    map.put(key, a);
                    a = new Accessibility(a);
                }
                key = MessageFormat.format("{0}.{1}", a.getName(), ClaimType.INSURER_VS_INSURER.name());
                if (map.containsKey(key)) {
                    addRolesToAccessibility(map.get(key), a);                    
                } else {
                    map.put(key, a);
                    a = new Accessibility(a);
                }
                key = MessageFormat.format("{0}.{1}", a.getName(), ClaimType.TPI.name());
                if (map.containsKey(key)) {
                    addRolesToAccessibility(map.get(key), a);                    
                } else {
                    map.put(key, a);
                    a = new Accessibility(a);
               }
                key = MessageFormat.format("{0}.{1}", a.getName(), ClaimType.INSURER_UPLOAD.name());
                if (map.containsKey(key)) {
                    addRolesToAccessibility(map.get(key), a);                    
                } else {
                    map.put(key, a);
                    a = new Accessibility(a);
                }
            } else {
                key = MessageFormat.format("{0}.{1}", a.getName(), a.getClaimType().name());
                if (map.containsKey(key)) {
                    addRolesToAccessibility(map.get(key), a);
                } else {
                    map.put(key, a);
                    a = new Accessibility(a);
                }
            }
        }

        LOG.info("AccessibilityByClaimTypeMap loaded: {} entries", map.size());
        return map;
    }


    @Override
    public Map<String, Accessibility> getAccessibilityMap() {

        Map<String, Accessibility> map = new HashMap<String, Accessibility>(ACCESSIBILITY_MAP_SIZE);
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        // Not interested in actions that are broken down by claim type
        c.add(Restrictions.conjunction()
                .add(Restrictions.not(Restrictions.like("name", "batch.%")))
                .add(Restrictions.not(Restrictions.like("name", "activity.%")))
                .add(Restrictions.not(Restrictions.like("name", "extraAction.%")))
                .add(Restrictions.not(Restrictions.like("name", "notification.%")))
                .add(Restrictions.not(Restrictions.like("name", "tab.%"))));

        List<Accessibility> accessibilities = findByCriteria(c);

        for (Accessibility a : accessibilities) {
            this.evict(a);
            map.put(a.getName(), a);
        }

        LOG.info("AccessibilityMap loaded: {} entries", map.size());
        return map;
    }

}
