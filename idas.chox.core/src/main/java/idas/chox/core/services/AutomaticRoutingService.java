package idas.chox.core.services;

import idas.chox.core.model.AutomaticRouting;
import java.util.List;

public interface AutomaticRoutingService {

    public List<AutomaticRouting> getAutomaticRoutings(int insurerId, int workgroupId);

    public List<AutomaticRouting> getAutomaticRoutings(int insurerId);

    public boolean isAutomaticRoutingExist(int insurerId, int workgroupId);

    public AutomaticRouting getAutomaticRouting(int insurerId, int workgroupId);

    public AutomaticRouting getAutomaticRouting(int automaticRoutingId);

    public void saveAutomaticRouting(AutomaticRouting automaticRouting);

    public void deleteAutomaticRouting(AutomaticRouting automaticRouting);
}
