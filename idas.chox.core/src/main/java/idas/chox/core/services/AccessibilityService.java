package idas.chox.core.services;

import idas.chox.core.model.Accessibility;
import java.util.HashMap;
import java.util.List;

public interface AccessibilityService {

    public List<Accessibility> getBatchUpdateAccessibilityMap(String accessibilityKey);

    public Accessibility getAccessibility(String accessibilityKey);

    public HashMap getAccessibilityMap();

    public HashMap getAccessibilityKeyMap();
}
