package chox.services;

import chox.model.AutomaticRouting;
import java.util.List;

public interface AutomaticRoutingService {
    public Boolean saveObj(AutomaticRouting obj);
    public AutomaticRouting getObject(int id);
    public List<AutomaticRouting> getObjects(int insurerId);
}