package idas.chox.core.services;

import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.AutomaticRoutingPrice;
import java.util.List;

public interface AutomaticRoutingService {

    public List<AutomaticRouting> getAutomaticRoutings(int insurerId, int workgroupId);

    public List<AutomaticRouting> getAutomaticRoutings(int insurerId);

    public boolean isWorkgroupInUseByAutomaticRouting(int workgroupId);

    public AutomaticRouting getAutomaticRouting(int insurerId, int workgroupId);

    public AutomaticRouting getAutomaticRouting(int automaticRoutingId);

    public void saveAutomaticRouting(AutomaticRouting automaticRouting);

    public void deleteAutomaticRouting(AutomaticRouting automaticRouting);


    public List<AutomaticRoutingPrice> getAutomaticRoutingsByPrice(int insurerId, int workgroupId);

    public List<AutomaticRoutingPrice> getAutomaticRoutingsByPrice(int insurerId);

    public AutomaticRoutingPrice getAutomaticRoutingByPrice(int automaticRoutingId);

}
