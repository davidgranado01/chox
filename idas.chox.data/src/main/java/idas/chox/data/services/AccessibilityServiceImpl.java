package idas.chox.data.services;

import idas.chox.core.model.Accessibility;
import idas.chox.core.model.AccessibilityItem;
import idas.chox.core.services.AccessibilityService;
import java.util.HashMap;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class AccessibilityServiceImpl extends BaseDataService implements AccessibilityService {

    @Override
    public List<Accessibility> getBatchUpdateAccessibilityMap(String accessibilityKey) {
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        c.add(Restrictions.like("name", accessibilityKey + ".%"));
        return findByCriteria(c);
    }

    @Override
    public HashMap getAccessibilityKeyMap() {

        HashMap map = new HashMap();
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        List result = findByCriteria(c);

        for (Object o : result) {
            Accessibility a = (Accessibility) o;
            map.put(a.getName(), null);
        }

        return map;
    }

    @Override
    public HashMap getAccessibilityMap() {

        HashMap map = new HashMap();
        DetachedCriteria c = DetachedCriteria.forClass(Accessibility.class);
        List result = findByCriteria(c);

        for (Object o : result) {
            Accessibility a = (Accessibility) o;

            HashMap roleMap = new HashMap();
            for (Object item : a.getAccessibilityItem()) {

                AccessibilityItem aItem = (AccessibilityItem) item;
                roleMap.put(aItem.getRole().trim(), aItem.getAccessRight());
            }

            map.put(a.getName(), roleMap);
        }

        return map;
    }

    @Override
    public Accessibility getAccessibility(String accessibilityKey) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Accessibility.class);
        criteria.add(Restrictions.eq("name", accessibilityKey));
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
