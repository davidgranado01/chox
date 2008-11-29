/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.Accessibility;
import chox.model.AccessibilityItem;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;

/**
 *
 * @author Emmanuel
 */
public class AccessibilityServiceImpl extends DataService implements AccessibilityService {

    public HashMap getAccessibilityMap() {
        HashMap map = new HashMap();

        Criteria c = currentSession.createCriteria(Accessibility.class);
        for (Object o : c.list()) {
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

    public void AddNewAccessibility(List<Accessibility> aList, short right) {
        
        currentSession.beginTransaction();
        for (Accessibility a : aList) {

            AccessibilityItem item = new AccessibilityItem();
            item.setAccessibility(a);
            item.setRole("ALL");
            item.setAccessRight(right);
            Set items = new HashSet();
            items.add(item);
            
            a.setAccessibilityItem(items);
            
            currentSession.save(a);
            
        }
       currentSession.getTransaction().commit();

    }
}
