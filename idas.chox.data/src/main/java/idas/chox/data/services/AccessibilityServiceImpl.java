/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.model.Accessibility;
import idas.chox.core.model.AccessibilityEditable;
import idas.chox.core.model.AccessibilityItem;
import idas.chox.core.services.AccessibilityService;
import java.util.HashMap;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class AccessibilityServiceImpl extends DataService implements AccessibilityService {

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

    private int getAccessibilityId(String accessibilityKey) {

        int oResult = -1;

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Accessibility.class);
            criteria.add(Restrictions.eq("name", accessibilityKey));
            Accessibility object = (Accessibility) getByCriteria(criteria);

            if (object != null) {
                oResult = object.getId();
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return oResult;
    }

    public AccessibilityEditable getAccessibilityEditable(String accessibilityKey) {

        AccessibilityEditable object = new AccessibilityEditable();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(AccessibilityEditable.class);
            criteria.add(Restrictions.eq("accessibility.id", getAccessibilityId(accessibilityKey)));
            object = (AccessibilityEditable) getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return object;

    }
}
