package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.AutomaticRoutingPolicy;
import idas.chox.core.model.AutomaticRoutingPrice;
import idas.chox.core.model.AutomaticRoutingCho;

public interface AutomaticRoutingService {

    List<AutomaticRoutingCho> getAutomaticRoutingsByCho(int insurerId, int choId);
    
    List<AutomaticRoutingPolicy> getAutomaticRoutingsByPolicy(int insurerId, int workgroupId);

    List<AutomaticRoutingPolicy> getAutomaticRoutingsByPolicy(int insurerId);

    boolean isWorkgroupInUseByAutomaticRouting(int workgroupId);

    AutomaticRoutingPolicy getAutomaticRouting(int insurerId, int workgroupId);

    AutomaticRoutingPolicy getAutomaticRouting(int automaticRoutingId);

    void saveAutomaticRouting(AutomaticRoutingPolicy automaticRouting);

    void deleteAutomaticRouting(AutomaticRoutingPolicy automaticRouting);

    List<AutomaticRoutingPrice> getAutomaticRoutingsByPrice(int insurerId, int workgroupId);

    List<AutomaticRoutingPrice> getAutomaticRoutingsByPrice(int insurerId);

    AutomaticRoutingPrice getAutomaticRoutingByPrice(int automaticRoutingId);

    void deleteAutomaticRoutingByPrice(AutomaticRoutingPrice automaticRouting);

    void saveAutomaticRoutingByPrice(AutomaticRoutingPrice automaticRouting);

}
