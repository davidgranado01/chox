package idas.chox.core.services;

import java.util.List;
import java.util.Map;

import idas.chox.core.model.Accessibility;

public interface AccessibilityService {

    Map<String, List<Accessibility>> getBatchUpdateAccessibilityMap();
    Map<String, Accessibility> getAccessibilityByClaimTypeMap();
    Map<String, Accessibility> getAccessibilityMap();

}
