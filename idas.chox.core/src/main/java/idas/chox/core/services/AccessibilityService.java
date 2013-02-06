package idas.chox.core.services;

import java.util.HashMap;
import java.util.List;

import idas.chox.core.model.Accessibility;
import idas.chox.core.model.ClaimType;

public interface AccessibilityService {

    public List<Accessibility> getBatchUpdateAccessibilityMap(String accessibilityKey);

    public Accessibility getAccessibility(String accessibilityKey, ClaimType claimType);

    public HashMap<String, HashMap> getAccessibilityMap();

    public HashMap<String, ClaimType> getAccessibilityKeyMap();
}
