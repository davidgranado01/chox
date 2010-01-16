package idas.chox.core.services;

import idas.chox.core.model.Accessibility;
import idas.chox.core.model.AccessibilityEditable;
import java.util.HashMap;
import java.util.List;

public interface AccessibilityService {

    public List<Accessibility> getBatchUpdateAccessibilityMap(String accessibilityKey);

    public HashMap getAccessibilityMap();

    public AccessibilityEditable getAccessibilityEditable(String accessibilityKey);
}
