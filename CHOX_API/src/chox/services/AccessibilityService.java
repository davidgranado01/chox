package chox.services;

import chox.model.AccessibilityEditable;
import java.util.HashMap;

public interface AccessibilityService {    
    
    public HashMap getAccessibilityMap();
    public AccessibilityEditable getAccessibilityEditable(String accessibilityKey);

}
