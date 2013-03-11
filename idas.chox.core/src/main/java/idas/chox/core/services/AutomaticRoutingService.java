package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.AutomaticRoutingPrice;

public interface AutomaticRoutingService {

    List<AutomaticRouting> getAutomaticRoutings(int insurerId, int workgroupId);

    List<AutomaticRouting> getAutomaticRoutings(int insurerId);

    boolean isWorkgroupInUseByAutomaticRouting(int workgroupId);

    AutomaticRouting getAutomaticRouting(int insurerId, int workgroupId);

    AutomaticRouting getAutomaticRouting(int automaticRoutingId);

    void saveAutomaticRouting(AutomaticRouting automaticRouting);

    void deleteAutomaticRouting(AutomaticRouting automaticRouting);

    List<AutomaticRoutingPrice> getAutomaticRoutingsByPrice(int insurerId, int workgroupId);

    List<AutomaticRoutingPrice> getAutomaticRoutingsByPrice(int insurerId);

    AutomaticRoutingPrice getAutomaticRoutingByPrice(int automaticRoutingId);

    void deleteAutomaticRoutingByPrice(AutomaticRoutingPrice automaticRouting);

    void saveAutomaticRoutingByPrice(AutomaticRoutingPrice automaticRouting);

}
