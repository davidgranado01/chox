/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Accessibility;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public interface AccessibilityService {    
    
    public HashMap getAccessibilityMap();
    public void AddNewAccessibility(List<Accessibility> aList,short right);

}
