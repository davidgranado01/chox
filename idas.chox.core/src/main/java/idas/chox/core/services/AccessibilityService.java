package idas.chox.core.services;

import idas.chox.core.model.AccessibilityEditable;
import java.util.HashMap;

public interface AccessibilityService {

    public HashMap getAccessibilityMap();

    public AccessibilityEditable getAccessibilityEditable(String accessibilityKey);
}
