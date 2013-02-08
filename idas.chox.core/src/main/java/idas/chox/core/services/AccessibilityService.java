package idas.chox.core.services;

import java.util.List;
import java.util.Map;

import idas.chox.core.model.Accessibility;

public interface AccessibilityService {

    public Map<String, List<Accessibility>> getBatchUpdateAccessibilityMap();
    public Map<String, Accessibility> getAccessibilityByClaimTypeMap();
    public Map<String, Accessibility> getAccessibilityMap();

}
