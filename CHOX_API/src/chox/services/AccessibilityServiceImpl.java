/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.Accessibility;
import chox.model.AccessibilityItem;
import java.util.HashMap;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Emmanuel
 */
public class AccessibilityServiceImpl extends HibernateDaoSupport implements AccessibilityService {

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
    
    private List findByCriteria(final DetachedCriteria c) {

        return getHibernateTemplate().findByCriteria(c);
    }

    public void AddNewAccessibility(List<Accessibility> aList, short right) {
        
       
    }
}
