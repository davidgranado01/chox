package idas.chox.core.services;

import idas.chox.core.model.AutomaticRouting;
import java.util.List;

public interface AutomaticRoutingService {

    public List<AutomaticRouting> getAutomaticRoutings(int insurerId);
}
